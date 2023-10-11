package com.example.screen_map

data class MapUiState(
    val currentPosition: Int,
    val selectedMarker: MarkerData? = null
)