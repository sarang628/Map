package com.example.screen_map.compose

import com.example.screen_map.data.MarkerData

data class MapUIState(
    val list: List<MarkerData> = listOf(),
    val isMapLoaded: Boolean = false,
    val currentPosition: Int = 0,
    val selectedMarker : MarkerData? = null,
    val cameraPosition : Triple<Double, Double, Float>? = null,
    val findMyLocation : Boolean = false
)

val MapUIState.markers : List<MarkerData> get() {
    return this.list.filter {
        it.id != this.selectedMarker?.id
    }
}
