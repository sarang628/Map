package com.example.screen_map

import com.google.android.gms.maps.model.LatLng

data class MapUiState(
    val list: List<MarkerData>,
    val currentPosition: Int,
    val move: MarkerData
) {
    fun currentLatLng(): LatLng {
        return list.get(currentPosition).getLatLng()
    }
}