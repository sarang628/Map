package com.example.screen_map


data class MapUiState(
    val requestMyLocation: Boolean = false,
//    val searchedRestaurants: List<Restaurant> = ArrayList(),
    val position: Int = 0,
//    val selectedNationItem: NationItem? = null,
//    var currentLocation : Location? = null
)