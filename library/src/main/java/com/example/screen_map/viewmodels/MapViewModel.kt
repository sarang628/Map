package com.example.screen_map.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screen_map.data.MarkerData
import com.example.screen_map.usecase.GetMarkerListFlowUseCase
import com.example.screen_map.usecase.GetMarkerListUseCase
import com.example.screen_map.usecase.SavePositionUseCase
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val saveMapPositionUseCase: SavePositionUseCase,
    private val getMarkerListFlowUseCase: GetMarkerListFlowUseCase
) : ViewModel() {
    var uiState: MapUIstate by mutableStateOf(MapUIstate(list = listOf()))
        private set

    init {
        viewModelScope.launch {
            try {
                getMarkerListFlowUseCase.invoke().collect {
                    uiState = uiState.copy(list = it)
                }
            }catch (e : Exception){
                Log.e("__MapViewModel", e.toString())
            }
        }
    }

    fun saveCameraPosition(state: CameraPositionState) {
        saveMapPositionUseCase.save(position = state.position)
        state.projection?.visibleRegion?.let {
            saveMapPositionUseCase.saveBound(it)
        }
    }

    fun getLastPosition(): LatLng {
        return saveMapPositionUseCase.load().target
    }

    fun getLastZoom(): Float {
        return saveMapPositionUseCase.load().zoom
    }

    fun onMapLoaded() {
        uiState = uiState.copy(isMapLoaded = true)
    }
}

data class MapUIstate(
    val list: List<MarkerData>,
    val isMapLoaded: Boolean = false,
    val currentPosition: Int = 0,
    val selectedMarker: MarkerData? = null
)