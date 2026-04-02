package com.example.screen_map.compose

import com.example.screen_map.data.MarkerData

data class MapUIState(
    val list: List<MarkerData> = listOf(),
    val isMapLoaded: Boolean = false,
    val currentPosition: Int = 0,
)
