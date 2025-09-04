package com.example.screen_map.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.icon
import com.example.screen_map.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
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
fun MapScreenForFinding(mapViewModel: MapViewModel = hiltViewModel(),
                        cameraSpeed: Int = 300,
                        cameraPositionState: CameraPositionState,
                        onMapClick: (LatLng) -> Unit = {},
                        myLocation: LatLng? = null,
                        boundary: Double? = null,
                        logoBottomPadding : Dp = 0.dp, ) {
    val selectedMarker = rememberMarkerState().apply { showInfoWindow() }
    val isMapLoaded = mapViewModel.uiState.isMapLoaded
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    var zoomLevel by remember { mutableFloatStateOf(cameraPositionState.position.zoom) } // 카메라의 줌 레벨을 추적

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            zoomLevel = cameraPositionState.position.zoom
        }
    }

    LaunchedEffect(key1 = mapViewModel.uiState.selectedMarker) {
        if (!isMapLoaded) return@LaunchedEffect

        //카드가 포커스된 음식점에 맞춰 지도 이동시키기
        mapViewModel.uiState.selectedMarker?.let {
            if (selectedMarker.position != it.getLatLng()) {
                cameraPositionState.animate(update = CameraUpdateFactory.newLatLng(it.getLatLng()), durationMs = cameraSpeed)
            }
        }
    }

    MapScreen(
        mapViewModel = mapViewModel,
        cameraPositionState = cameraPositionState,
        onMapClick = onMapClick,
        logoBottomPadding = logoBottomPadding,
        onMapLoaded = { coroutine.launch {
            if (!isMapLoaded) { // 플래그 처리 안하면 지도화면으로 이동할때마다 이벤트 발생 처음에 한번만 동작하면 됨
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(mapViewModel.getLastPosition(), mapViewModel.getLastZoom()), durationMs = 1000)
                delay(1000) //카메라 이동 전까지 플래그 비활성화
                mapViewModel.onMapLoaded() }
        }}) {
        mapViewModel.uiState.list.let {
            for (data: MarkerData in it) {
                if(mapViewModel.uiState.selectedMarker?.title != data.title)
                Marker(tag = data.id, state = data.markState(), /*title = data.title,*/ snippet = data.snippet, onClick = { mapViewModel.onMark(Integer.parseInt(it.tag.toString())); false }, icon = data.icon(context, data.title, data.rating, false, data.price, zoomLevel > 16.8, zoomLevel > 16.8))
            }
        }
        mapViewModel.uiState.selectedMarker?.let {
            selectedMarker.position = it.getLatLng()
            Marker(state = selectedMarker, /*title = it.title,*/ snippet = it.snippet, onClick = { false }, tag = it.id, icon = it.icon(context, it.title, it.rating, isSelected = true, it.price))
        }

        myLocation?.let { latlng ->
            boundary?.let {
                Circle(center = latlng, radius = boundary, strokeWidth = 5f, strokeColor = MaterialTheme.colorScheme.primary)
            }
        }
    }
}