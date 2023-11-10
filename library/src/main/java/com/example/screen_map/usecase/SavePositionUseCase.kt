package com.example.screen_map.usecase

import com.google.android.gms.maps.model.CameraPosition

interface SavePositionUseCase {
    fun save(position: CameraPosition)
    fun load(): CameraPosition
}