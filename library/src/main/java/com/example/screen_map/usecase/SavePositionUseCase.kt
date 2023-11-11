package com.example.screen_map.usecase

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.VisibleRegion

interface SavePositionUseCase {
    fun save(position: CameraPosition)
    fun load(): CameraPosition
    fun saveBound(visibleRegion: VisibleRegion)
}