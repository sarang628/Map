package com.example.screen_map.usecase

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.StateFlow

interface CameraMoveUseCase {
    fun invoke() : StateFlow<Triple<Double, Double, Float?>?>
}