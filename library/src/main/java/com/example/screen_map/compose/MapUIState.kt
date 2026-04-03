package com.example.screen_map.compose

import androidx.compose.ui.text.font.Font
import com.example.screen_map.data.MarkerData
import com.google.android.gms.maps.model.LatLng

data class MapUIState(
    val list: List<MarkerData> = listOf(),
    val isMapLoaded: Boolean = false,
    val currentPosition: Int = 0,
    val selectedMarker : MarkerData? = null,
    val cameraPosition : Pair<LatLng, Float>? = null
)
