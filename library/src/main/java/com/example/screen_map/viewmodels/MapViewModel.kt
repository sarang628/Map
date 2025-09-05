package com.example.screen_map.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screen_map.data.MarkerData
import com.example.screen_map.usecase.FindRestaurantUseCase
import com.example.screen_map.usecase.GetMarkerListFlowUseCase
import com.example.screen_map.usecase.GetSelectedMarkUseCase
import com.example.screen_map.usecase.SavePositionUseCase
import com.example.screen_map.usecase.SetSelectedMarkUseCase
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val saveMapPositionUseCase : SavePositionUseCase,
    private val getMarkerListFlowUseCase : GetMarkerListFlowUseCase,
    private val getSelectedMarkUseCase : GetSelectedMarkUseCase,
    private val setSelectMarkerUseCase : SetSelectedMarkUseCase,
    private val findRestaurantUseCase : FindRestaurantUseCase
) : ViewModel() {
    var uiState: MapUIState by mutableStateOf(MapUIState(list = listOf())); private set

    init {
        viewModelScope.launch {
            launch {
                getMarkerListFlowUseCase.invoke(viewModelScope).collect { uiState = uiState.copy(list = it) }
            }
            launch {
                getSelectedMarkUseCase.invoke(viewModelScope).collect {
                    uiState = uiState.copy(selectedMarker = if(it.id == -1) null else it)
                }
            }
        }
    }

    fun findRestaurant(restaurantId: Int){
        viewModelScope.launch {
            val result = findRestaurantUseCase.invoke(restaurantId)
            uiState = uiState.copy(selectedMarker = result, list = listOf(result))
        }
    }

    fun saveCameraPosition(state: CameraPositionState) {
        saveMapPositionUseCase.save(position = state.position)
        state.projection?.visibleRegion?.let { saveMapPositionUseCase.saveBound(it) }
    }
    fun getLastPosition(): LatLng { return saveMapPositionUseCase.load().target }
    fun getLastZoom(): Float { return saveMapPositionUseCase.load().zoom }
    fun onMapLoaded() { uiState = uiState.copy(isMapLoaded = true) }
    fun onMark(restaurantId: Int) { viewModelScope.launch { setSelectMarkerUseCase.invoke(restaurantId) } }
}

data class MapUIState(
    val list: List<MarkerData>,
    val isMapLoaded: Boolean = false,
    val currentPosition: Int = 0,
    val selectedMarker: MarkerData? = null
)