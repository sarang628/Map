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
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
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
fun MapScreenForRestaurant(
    mapViewModel: MapViewModel = hiltViewModel(),
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    selectedMarkerData: MarkerData?,
    mapUiSettings: MapUiSettings = MapUiSettings(
        zoomControlsEnabled = true
    ),
    onMapClick: (LatLng) -> Unit = {},
    zoom : Float
) {
    val isMapLoaded by mapViewModel.isMapLoaded.collectAsState()
    val coroutine = rememberCoroutineScope()
    MapScreen(
        mapViewModel = mapViewModel,
        cameraPositionState = cameraPositionState,
        selectedMarkerData = selectedMarkerData,
        onMapClick = onMapClick,
        uiSettings = mapUiSettings,
        onMapLoaded = {
            coroutine.launch {
                if (!isMapLoaded) { // 플래그 처리 안하면 지도화면으로 이동할때마다 이벤트 발생 처음에 한번만 동작하면 됨
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                selectedMarkerData?.lat ?: 0.0,
                                selectedMarkerData?.lon ?: 0.0
                            ),
                            zoom
                        ),
                        durationMs = 1000
                    )
                    //카메라 이동 전까지 플래그 비활성화
                    delay(1000)
                    mapViewModel.onMapLoaded()
                }
            }
        }
    )
}