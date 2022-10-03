package com.example.screen_map

import com.sryang.torang_core.data.NationItem
import com.sryang.torang_core.data.entity.Location
import com.sryang.torang_core.data.entity.Restaurant


data class MapUiState(
    val requestMyLocation: Boolean = false,
    val searchedRestaurants: List<Restaurant> = ArrayList(),
    val position: Int = 0,
    val selectedNationItem: NationItem? = null,
    var currentLocation : Location? = null
)