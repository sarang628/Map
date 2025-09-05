package com.example.screen_map.usecase

import com.example.screen_map.data.MarkerData

interface FindRestaurantUseCase {
    suspend fun invoke(restaurantId : Int) : MarkerData
}