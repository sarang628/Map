package com.example.screen_map

import kotlinx.coroutines.flow.Flow

interface MapService {
    suspend fun restaurantMarkerList(): List<MarkerData>
}