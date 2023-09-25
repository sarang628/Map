package com.example.screen_map

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MapViewModel : ViewModel() {

    @Inject
    lateinit var mapService: MapService

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
        /*viewModelScope.launch {
            mapUiStateFlow.emit(
                mapUiStateFlow.value.copy(
                    list = list.stream().map { it.toMarkerData() }.toList()
                )
            )
        }*/
    }

    /*fun selectRestaurant(restaurant: Restaurant) {
        val r = list.find {
            it.restaurant_id == restaurant.restaurant_id
        }
        viewModelScope.launch {
            mapUiStateFlow.emit(
                mapUiStateFlow.value.copy(
                    move = r?.toMarkerData()
                )
            )
        }
    }*/

}

/*
fun Restaurant.toMarkerData(): MarkerData {
    return MarkerData(
        id = restaurant_id ?: 0,
        lat = lat ?: 0.0,
        lon = lon ?: 0.0,
        title = restaurant_name ?: ""
    )
}*/
