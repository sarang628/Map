package com.example.screen_map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState

data class MarkerData(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val title: String = "",
    val snippet: String = ""
) {
    fun markState(): MarkerState {
        return MarkerState(position = getLatLng())
    }

    fun getLatLng(): LatLng {
        return LatLng(lat, lon)
    }
}
