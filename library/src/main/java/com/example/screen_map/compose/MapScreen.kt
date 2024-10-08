package com.example.screen_map.compose

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @param mapViewModel map 뷰모델
 * @param onMark map 마커 클릭 이벤트
 * @param cameraSpeed map 카메라 이동 속도 설정
 * @param cameraPositionState map 카메라 위치 상태 객체
 * @param list 지도에 마킹 할 데이터
 * @param selectedMarkerData 선택된 마커. 외부에서 마커로 위치시키고 싶을 때 사용
 * @param onMapClick 맵 클릭 이벤트
 * @param myLocation 내 위치로 이동
 * @param boundary 내 위치 반경 표시
 */
@Composable
fun MapScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    onMark: ((Int) -> Unit)? = null,
    cameraPositionState: CameraPositionState,
    selectedMarkerData: MarkerData?,
    onMapClick: (LatLng) -> Unit = {},
    uiSettings: MapUiSettings = MapUiSettings(
        zoomControlsEnabled = false,
        myLocationButtonEnabled = false,
        compassEnabled = false
    ),
    onMapLoaded: () -> Unit = {},
    content: (@Composable @GoogleMapComposable () -> Unit)? = null,
) {
    val context = LocalContext.current
    val selectedMarker = rememberMarkerState().apply { showInfoWindow() }
    val isMyLocationEnabled =
        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = isMyLocationEnabled)) }
    val isMapLoaded by mapViewModel.isMapLoaded.collectAsState()

    LaunchedEffect(key1 = cameraPositionState, block = {
        snapshotFlow { cameraPositionState.isMoving }.collect {
            if (!cameraPositionState.isMoving) {
                // 맵이 로드될 때 0,0 좌표를 저장하는 이벤트 발생하여 방어로직추가
                if (isMapLoaded) {
                    //마지막으로 움직인 지점 저장하기
                    mapViewModel.saveCameraPosition(cameraPositionState)
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
                    durationMs = 300
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
            uiSettings = uiSettings,
            onMapLoaded = onMapLoaded
        ) {
            content?.invoke()
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
        }
        if (!isMapLoaded) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) { }
                    .background(Color(0x33000000))
            ) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}