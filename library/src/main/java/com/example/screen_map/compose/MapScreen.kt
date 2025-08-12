package com.example.screen_map.compose

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.data.icon
import com.example.screen_map.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState

/**
 * @param mapViewModel map 뷰모델
 * @param onMark map 마커 클릭 이벤트
 * @param cameraPositionState map 카메라 위치 상태 객체
 * @param selectedMarkerData 선택된 마커. 외부에서 마커로 위치시키고 싶을 때 사용
 * @param onMapClick 맵 클릭 이벤트
 */
@Composable
fun MapScreen(mapViewModel: MapViewModel = hiltViewModel(), onMark: ((Int) -> Unit) = {}, cameraPositionState: CameraPositionState, onMapClick: (LatLng) -> Unit = {}, uiSettings: MapUiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false, compassEnabled = false), onMapLoaded: () -> Unit = {}, logoBottomPadding : Dp = 0.dp, content: (@Composable @GoogleMapComposable () -> Unit) = { }) {
    val context = LocalContext.current
    val selectedMarker = rememberMarkerState().apply { showInfoWindow() }
    val isMyLocationEnabled = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = isMyLocationEnabled)) }

    LaunchedEffect(key1 = cameraPositionState, block = {
        snapshotFlow { cameraPositionState.isMoving }.collect {
            if (!cameraPositionState.isMoving) { // 맵이 로드될 때 0,0 좌표를 저장하는 이벤트 발생하여 방어로직추가
                if (mapViewModel.uiState.isMapLoaded) { //마지막으로 움직인 지점 저장하기
                    mapViewModel.saveCameraPosition(cameraPositionState)
                }
            }
        }
    })

    LaunchedEffect(key1 = mapViewModel.uiState.selectedMarker) {
        if (!mapViewModel.uiState.isMapLoaded) return@LaunchedEffect

        mapViewModel.uiState.selectedMarker?.let { //카드가 포커스된 음식점에 맞춰 지도 이동시키기
            if (selectedMarker.position != it.getLatLng()) {
                cameraPositionState.animate(update = CameraUpdateFactory.newLatLng(it.getLatLng()), durationMs = 300)
            }
        }
    }

    Box {
        GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState, properties = mapProperties, onMapClick = onMapClick, uiSettings = uiSettings, onMapLoaded = { onMapLoaded.invoke(); mapViewModel.onMapLoaded() }, contentPadding = PaddingValues(bottom = logoBottomPadding)) {
            content.invoke()
            /*mapViewModel.uiState.selectedMarker?.let {
                selectedMarker.position = it.getLatLng()
                Marker(state = selectedMarker, title = it.title, snippet = it.snippet, onClick = { onMark.invoke(Integer.parseInt(it.tag.toString())); false }, tag = it.id, icon = it.icon(context, "", ""))
            }*/
        }
        if (!mapViewModel.uiState.isMapLoaded) {
            Box(Modifier.fillMaxSize().clickable(enabled = false) { }.background(Color(0x33000000))) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}