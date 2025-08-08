package com.example.screen_map.usecase

import com.example.screen_map.data.MarkerData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface GetSelectedMarkUseCase {
    fun invoke(coroutineScope: CoroutineScope): StateFlow<MarkerData>
}