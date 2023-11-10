package com.example.screen_map.usecase

import com.example.screen_map.data.MarkerData

interface MapService {
    suspend fun restaurantMarkerList(): List<MarkerData>
}