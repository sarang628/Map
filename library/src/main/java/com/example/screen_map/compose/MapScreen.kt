package com.example.screen_map.compose

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.booleanResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.icon
import com.example.screen_map.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.Polyline
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.delay
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
    myLocation: LatLng? = null,
    boundary : Double? = null
) {
    val context = LocalContext.current
    val selectedMarker = rememberMarkerState().apply { showInfoWindow() }
    var isMapLoaded by remember { mutableStateOf(false) }
    var isMyLocationEnabled =
        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    val coroutine = rememberCoroutineScope()

    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = isMyLocationEnabled)) }


    LaunchedEffect(key1 = cameraPositionState, block = {
        snapshotFlow { cameraPositionState.isMoving }.collect {
            if (!cameraPositionState.isMoving) {
                // 맵이 로드될 때 0,0 좌표를 저장하는 이벤트 발생하여 방어로직추가
                if (isMapLoaded) {
                    //마지막으로 움직인 지점 저장하기
                    mapViewModel.saveCameraPosition(cameraPositionState.position)
                }
            }
        }
    })

    LaunchedEffect(key1 = selectedMarkerData) {
        if (!isMapLoaded)
            return@LaunchedEffect

        //카드가 포커스된 음식점에 맞춰 지도 이동시키기
        selectedMarkerData?.let {
            if (selectedMarker.position != it.getLatLng()) {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLng(it.getLatLng()),
                    durationMs = speed
                )
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
                coroutine.launch {
                    if (!isMapLoaded) { // 플래그 처리 안하면 지도화면으로 이동할때마다 이벤트 발생 처음에 한번만 동작하면 됨
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(
                                mapViewModel.getLastPosition(),
                                mapViewModel.getLastZoom()
                            ),
                            durationMs = 1000
                        )
                        //카메라 이동 전까지 플래그 비활성화
                        delay(1000)
                        isMapLoaded = true
                    }
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

            myLocation?.let { latlng->
                boundary?.let {
                    Circle(
                        center = latlng,
                        radius = boundary,
                        strokeWidth = 5f,
                        strokeColor = MaterialTheme.colorScheme.primary
                    )
                }
                }
        }
        if (!isMapLoaded) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable(
                        enabled = false
                    ) { }
                    .background(
                        Color(0x33000000)
                    )
            ) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}