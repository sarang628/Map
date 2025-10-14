package com.example.screen_map.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

data class MapScreenCallback(
    val onSaveCameraPosition      : (CameraPositionState) -> Unit               = {},
    val onMapClick                : (LatLng) -> Unit                            = {},
    val onMapLoaded               : () -> Unit                                  = {},
    val onMark                    : (Int) -> Unit                               = {},
)