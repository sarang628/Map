package com.example.screen_map.compose

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.icon
import com.example.screen_map.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
fun MapScreenForFinding(mapViewModel: MapViewModel = hiltViewModel(), onMark: ((Int) -> Unit) = {}, cameraSpeed: Int = 300, cameraPositionState: CameraPositionState, selectedMarkerData: MarkerData?, onMapClick: (LatLng) -> Unit = {}, myLocation: LatLng? = null, boundary: Double? = null,
) {
    val selectedMarker = rememberMarkerState().apply { showInfoWindow() }
    val isMapLoaded by mapViewModel.isMapLoaded.collectAsState()
    val coroutine = rememberCoroutineScope()


    LaunchedEffect(key1 = selectedMarkerData) {
        if (!isMapLoaded)
            return@LaunchedEffect

        //카드가 포커스된 음식점에 맞춰 지도 이동시키기
        selectedMarkerData?.let {
            if (selectedMarker.position != it.getLatLng()) {
                cameraPositionState.animate(update = CameraUpdateFactory.newLatLng(it.getLatLng()), durationMs = cameraSpeed)
            }
        }
    }

    MapScreen(
        mapViewModel = mapViewModel,
        cameraPositionState = cameraPositionState,
        selectedMarkerData = selectedMarkerData,
        onMapClick = onMapClick,
        onMark = onMark,
        onMapLoaded = { coroutine.launch {
            if (!isMapLoaded) { // 플래그 처리 안하면 지도화면으로 이동할때마다 이벤트 발생 처음에 한번만 동작하면 됨
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(mapViewModel.getLastPosition(), mapViewModel.getLastZoom()), durationMs = 1000)
                delay(1000) //카메라 이동 전까지 플래그 비활성화
                mapViewModel.onMapLoaded() }
        }}) {
        mapViewModel.uiState.list.let {
            Log.d("__MapScreenForFinding", "markerSize : ${it.size}")
            for (data: MarkerData in it) {
                Marker(tag = data.id, state = data.markState(), title = data.title, snippet = data.snippet, onClick = { onMark.invoke(Integer.parseInt(it.tag.toString())); false }, icon = BitmapDescriptorFactory.fromResource(data.icon))
            }
        }

        myLocation?.let { latlng ->
            boundary?.let {
                Circle(center = latlng, radius = boundary, strokeWidth = 5f, strokeColor = MaterialTheme.colorScheme.primary)
            }
        }
    }
}