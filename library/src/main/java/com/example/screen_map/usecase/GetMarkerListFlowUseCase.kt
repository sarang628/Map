package com.example.screen_map.usecase

import com.example.screen_map.data.MarkerData
import kotlinx.coroutines.flow.StateFlow

interface GetMarkerListFlowUseCase {
    suspend fun invoke(): StateFlow<List<MarkerData>>
}