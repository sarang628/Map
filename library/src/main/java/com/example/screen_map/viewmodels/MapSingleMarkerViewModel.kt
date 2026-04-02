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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapSingleMarkerViewModel @Inject constructor(
    private val findRestaurantUseCase : FindRestaurantUseCase
) : ViewModel() {
    var uiState: MapSingleMarkerUIState by mutableStateOf(MapSingleMarkerUIState()); private set

    fun selectRestaurant(restaurantId: Int) {
        viewModelScope.launch {
            val result = findRestaurantUseCase.invoke(restaurantId)

            uiState = uiState.copy(restaurantId = restaurantId,
                selectedMarker = result)
        }
    }

    fun onMapLoaded() {
        uiState = uiState.copy(isMapLoaded = true)
    }
}

data class MapSingleMarkerUIState(
    val isMapLoaded: Boolean = false,
    val currentPosition: Int = 0,
    val restaurantId: Int = -1,
    val selectedMarker : MarkerData? = null
)