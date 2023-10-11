package com.example.screen_map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    animationMoveDuration: Int,
    onMark: ((Int) -> Unit)? = null,
    onIdle: () -> Unit,
    cameraPositionState: CameraPositionState,
    list: List<MarkerData>?,
    selectedMarkerData: MarkerData?
) {

    val uiState: MapUiState by mapViewModel.mapUiStateFlow.collectAsState()
    val selectedMarker = rememberMarkerState().apply {
        showInfoWindow()
    }

    // 맵에서 마커를 클릭 시 카드를 움직이게되는데
    // 이 때 카드가 움직이며 또 맵의 마커 이동 요청을 하게됨으로 이를 방지하기위해
    // 먼저 맵의 idle상태를 전달하기로 결정
    LaunchedEffect(key1 = cameraPositionState, block = {
        snapshotFlow { cameraPositionState.isMoving }.collect {
            if (!it) {
                Log.d("MapScreen", "idle")
                onIdle.invoke()
            }
        }
    })

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        selectedMarkerData?.let {
            Marker(
                state = selectedMarker,
                title = selectedMarkerData.title,
                snippet = selectedMarkerData.snippet,
                onClick = {
                    onMark?.invoke(Integer.parseInt(it.tag.toString()))
                    false
                },
                tag = selectedMarkerData.id,
                icon = BitmapDescriptorFactory.fromResource(selectedMarkerData.icon)
            )
        }

        list?.let {
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
}