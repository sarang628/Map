package com.example.screen_map.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.example.screen_map.data.MarkerData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.flow.distinctUntilChanged

class MapState(
    val cameraPositionState: CameraPositionState,
) {
    var onMapLoaded by mutableStateOf(false)
        private set

    fun setOnMapLoaded(){
        onMapLoaded = true
    }
}

private const val tag : String = "__rememberMapState"
@Composable
fun rememberMapState() : MapState{

    val cameraPositionState = rememberCameraPositionState()

    return MapState(
        cameraPositionState = cameraPositionState,
    )
}