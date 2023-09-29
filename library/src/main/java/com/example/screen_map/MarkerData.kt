package com.example.screen_map

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState

data class MarkerData(
    val id : Int,
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val title: String = "",
    val snippet: String = ""
) {
    @Composable
    fun markState(): MarkerState {
        val markState = MarkerState(position = getLatLng())
        return markState
    }

    fun getLatLng(): LatLng {
        return LatLng(lat, lon)
    }
}
