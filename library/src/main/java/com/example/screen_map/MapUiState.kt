package com.example.screen_map

import com.google.android.gms.maps.model.LatLng
import kotlin.streams.toList

data class MapUiState(
    val list: List<MarkerData>,
    val currentPosition: Int,
    val selectedMarker: MarkerData? = null
) {
    fun currentLatLng(): LatLng {
        return list.get(currentPosition).getLatLng()
    }
}

val MapUiState.unSelectedMarkers: List<MarkerData>
    get() = this.list.stream().filter {
        (this.selectedMarker?.id ?: -1) != it.id
    }.toList()