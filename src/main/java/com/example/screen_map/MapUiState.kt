package com.example.screen_map

import com.example.torang_core.data.model.Restaurant

data class MapUiState(
    val requestMyLocation: Boolean = false,
    val searchedRestaurants: List<Restaurant> = ArrayList(),
    val position: Int = 0
)