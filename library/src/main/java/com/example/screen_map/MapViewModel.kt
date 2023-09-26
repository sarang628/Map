package com.example.screen_map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapService: MapService
) : ViewModel() {


    val mapUiStateFlow = MutableStateFlow(
        MapUiState(
            list = ArrayList(),
            currentPosition = 0,
            move = MarkerData(
                id = 0,
                lat = 0.0,
                lon = 0.0,
                title = "",
                snippet = ""
            )
        )
    )

    init {
        Log.d("_MapViewModel", "init")
        viewModelScope.launch {
            val list = mapService.restaurantMarkerList()
            Log.d("_MapViewModel", list.toString())
            mapUiStateFlow.emit(
                mapUiStateFlow.value.copy(
                    list = list
                )
            )
        }
    }

    fun selectRestaurant(restaurant: MarkerData) {
        val r = mapUiStateFlow.value.list.find {
            it.id == restaurant.id
        }
        viewModelScope.launch {
            r?.let {
                mapUiStateFlow.emit(
                    mapUiStateFlow.value.copy(
                        move = it
                    )
                )
            }
        }
    }

}