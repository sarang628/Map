package com.example.screen_map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    uiStateFlow: StateFlow<MapUiState>,
    animationMoveDuration: Int,
    onMark: ((Int) -> Unit)? = null,
    onIdle: () -> Unit
) {

    val uiState: MapUiState by mapViewModel.mapUiStateFlow.collectAsState()
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

    // 맵에서 마커를 클릭 시 카드를 움직이게되는데
    // 이 때 카드가 움직이며 또 맵의 마커 이동 요청을 하게됨으로 이를 방지하기위해
    // 먼저 맵의 idle상태를 전달하기로 결정
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
                },
                icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_korean)
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
                    icon = BitmapDescriptorFactory.fromResource(data.icon)
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