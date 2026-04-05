package com.example.screen_map.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screen_map.compose.MapUIState
import com.example.screen_map.data.MarkerData
import com.example.screen_map.usecase.CameraMoveUseCase
import com.example.screen_map.usecase.GetMarkerListFlowUseCase
import com.example.screen_map.usecase.GetSelectedMarkUseCase
import com.example.screen_map.usecase.SavePositionUseCase
import com.example.screen_map.usecase.SetSelectedMarkUseCase
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val tag = "__MapViewModel"
@HiltViewModel
open class MapViewModel @Inject constructor(
    private val saveMapPositionUseCase : SavePositionUseCase,
    private val getMarkerListFlowUseCase : GetMarkerListFlowUseCase,
    private val getSelectedMarkUseCase : GetSelectedMarkUseCase,
    private val setSelectMarkerUseCase : SetSelectedMarkUseCase,
    private val cameraMoveUseCase      : CameraMoveUseCase
) : ViewModel() {
    var uiState: MapUIState by mutableStateOf(MapUIState(list = listOf())); private set
    init {
        viewModelScope.launch {
            launch {
                // 마커 리스트(검색된 음식점)
                getMarkerListFlowUseCase.invoke(viewModelScope).collect {
                    uiState = uiState.copy(list = it)
                }
            }
            launch {
                // 선택된 마커(음식점)
                getSelectedMarkUseCase.invoke(viewModelScope).collect {

                    if(uiState.selectedMarker?.id != it.id) {
                        Log.d(tag, "selected restaurant : ${it.id}")
                        uiState = uiState.copy(selectedMarker = if (it.id == -1) null else it)
                    }else{
                        Log.d(tag, "already selected. marker doesn't move")
                    }
                }
            }
            launch {
                cameraMoveUseCase.invoke().collect {
                    setCameraPosition(position = it)
                }
            }
        }
    }

    fun setCameraPosition(position : Triple<Double, Double, Float>?){
        uiState = uiState.copy(cameraPosition = position)
    }

    fun saveCameraPosition(state: CameraPositionState) {
        saveMapPositionUseCase.save(position = state.position)
        state.projection?.visibleRegion?.let { saveMapPositionUseCase.saveBound(it) }
    }
    fun getLastPosition(): LatLng { return saveMapPositionUseCase.load().target }
    fun getLastZoom(): Float { return saveMapPositionUseCase.load().zoom }
    fun onMapLoaded() { uiState = uiState.copy(isMapLoaded = true) }
    fun onMark(restaurantId: Int) {
        uiState.list.firstOrNull { it.id == restaurantId }?.let {
            Log.d(tag, "onMark: $it")
            uiState = uiState.copy(selectedMarker = it)
        } ?: run {
            Log.e(tag, "failed selected marker. not found in markerList restaurantId: ${restaurantId}")
        }
        viewModelScope.launch { setSelectMarkerUseCase.invoke(restaurantId) }
    }
}