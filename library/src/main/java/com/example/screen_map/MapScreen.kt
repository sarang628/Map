package com.example.screen_map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    uiStateFlow: StateFlow<MapUiState>,
    animationMoveDuration: Int,
    onMark: ((Int) -> Unit)? = null,
    onIdle: () -> Unit
) {

    val uiState by mapViewModel.mapUiStateFlow.collectAsState()
    val list = ArrayList<MarkerState>()
    val markerState = rememberMarkerState().apply {
        showInfoWindow()
    }

    val cameraPositionState = rememberCameraPositionState {
        uiState.list.let {
            if (it.isNotEmpty()) {
                position = CameraPosition.fromLatLngZoom(
                    it[0].getLatLng(), 15f
                )
            }
        }
    }

    LaunchedEffect(key1 = cameraPositionState, block = {
        snapshotFlow { cameraPositionState.isMoving }.collect {
            if (!it)
                onIdle.invoke()
        }
    })

    val scope = rememberCoroutineScope()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        if (markerState.position.latitude != 0.0)
            Marker(
                state = markerState,
                title = uiState.move.title,
                snippet = uiState.move.snippet,
                onClick = {
                    onMark?.invoke(Integer.parseInt(it.tag.toString()))
                    false
                }
            )

        uiState.list.let {
            for (data: MarkerData in it) {
                Marker(
                    state = data.markState(),
                    title = data.title,
                    snippet = data.snippet,
                    onClick = {
                        onMark?.invoke(Integer.parseInt(it.tag.toString()))
                        false
                    },
                    tag = data.id,
                )
            }
        }
    }

    uiState.move.let {
        scope.launch {
            markerState.position = it.getLatLng()
            markerState.hideInfoWindow()
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLng(uiState.move.getLatLng()),
                durationMs = animationMoveDuration
            )
            markerState.showInfoWindow()
        }
    }
}