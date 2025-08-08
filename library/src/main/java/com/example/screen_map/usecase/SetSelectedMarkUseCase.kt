package com.example.screen_map.usecase

interface SetSelectedMarkUseCase {
    suspend fun invoke(restaurantId : Int)
}