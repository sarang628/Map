package com.example.screen_map.uistate

import com.example.screen_map.data.MarkerData

data class MapUiState(
    val currentPosition: Int,
    val selectedMarker: MarkerData? = null
)