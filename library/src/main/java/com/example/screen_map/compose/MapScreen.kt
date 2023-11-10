package com.example.screen_map.compose

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.icon
import com.example.screen_map.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    onMark: ((Int) -> Unit)? = null,
    speed: Int = 300,
    cameraPositionState: CameraPositionState,
    list: List<MarkerData>?,
    selectedMarkerData: MarkerData?,
    onMapClick: (LatLng) -> Unit = {},
) {
    val context = LocalContext.current
    val selectedMarker = rememberMarkerState().apply { showInfoWindow() }
    var isMapLoaded by remember { mutableStateOf(false) }
    var isFirst by remember { mutableStateOf(true) }
    var isMyLocationEnabled =
        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    val coroutine = rememberCoroutineScope()

    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = isMyLocationEnabled)) }

    /* 맵에서 마커를 클릭 시 카드를 움직이게되는데
    이 때 카드가 움직이며 또 맵의 마커 이동 요청을 하게됨으로 이를 방지하기위해
    먼저 맵의 idle상태를 전달하기로 결정 */
    LaunchedEffect(key1 = cameraPositionState, block = {
        snapshotFlow { cameraPositionState.isMoving }.collect {
            if (!cameraPositionState.isMoving) {
                Log.d("_MapScreen", cameraPositionState.position.toString())
                if (isMapLoaded) {
                    mapViewModel.saveCameraPosition(cameraPositionState.position)
                }
            }
        }
    })

    LaunchedEffect(key1 = selectedMarkerData) {
        selectedMarkerData?.let {
            if (selectedMarker.position != it.getLatLng()) {
                cameraPositionState.animate(
                    update = if (isFirst) CameraUpdateFactory.newLatLngZoom(
                        it.getLatLng(),
                        18f
                    ) else CameraUpdateFactory.newLatLng(it.getLatLng()),
                    durationMs = speed
                )
                isFirst = false
            }
        }
    }

    Box {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            onMapClick = onMapClick,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
                compassEnabled = false
            ),
            onMapLoaded = {
                isMapLoaded = true
                coroutine.launch {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            mapViewModel.getLastPosition(),
                            mapViewModel.getLastZoom()
                        ),
                        durationMs = 1000
                    )
                }
            }
        ) {
            selectedMarkerData?.let {
                selectedMarker.position = selectedMarkerData.getLatLng()
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
        if (!isMapLoaded) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0x55000000))
            ) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}