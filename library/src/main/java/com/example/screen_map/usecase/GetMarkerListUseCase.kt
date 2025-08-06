package com.example.screen_map.usecase

import com.example.screen_map.data.MarkerData

interface GetMarkerListUseCase {
    suspend fun invoke(): List<MarkerData>
}