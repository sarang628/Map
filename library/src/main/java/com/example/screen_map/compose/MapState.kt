package com.example.screen_map.compose

import androidx.compose.animation.core.snap
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
    val zoomLevel : Float,
    val selectedMarker : MarkerState
) {
    var onMapLoaded by mutableStateOf(false)
        private set

    fun setSelectMarker(data: MarkerData) {
        selectedMarker.position = LatLng(data.lat, data.lon)
    }

    fun setOnMapLoaded(){
        onMapLoaded = true
    }
}

@Composable
fun rememberMapState(
    tag : String = "__rememberMapState",
    showLog : Boolean = false
) : MapState{

    val cameraPositionState = rememberCameraPositionState()
    var zoomLevel by remember { mutableFloatStateOf(cameraPositionState.position.zoom) } // 카메라의 줌 레벨을 추적
    val selectedMarker      : MarkerState   = rememberMarkerState().apply { showInfoWindow() }

    // save zoom level
    LaunchedEffect("") {
        snapshotFlow { cameraPositionState.position.zoom }
            .distinctUntilChanged()
            .collect {
            if (!cameraPositionState.isMoving) {
                showLog.log(tag, "zoomLevel changed: $zoomLevel")
                //zoomLevel = cameraPositionState.position.zoom
            }
        }
    }

    /*LaunchedEffect(key1 = uiState.selectedMarker) {
        if (!uiState.isMapLoaded) return@LaunchedEffect

        uiState.selectedMarker?.let { //카드가 포커스된 음식점에 맞춰 지도 이동시키기
            if (selectedMarker.position != it.getLatLng()) {
                mapState.cameraPositionState.animate(update = CameraUpdateFactory.newLatLng(it.getLatLng()), durationMs = 300)
            }
        }
    }*/

    return MapState(
        cameraPositionState = cameraPositionState,
        zoomLevel = zoomLevel,
        selectedMarker = selectedMarker
    )
}