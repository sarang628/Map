package com.example.screen_map.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.data.MarkerData
import com.example.screen_map.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
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
fun MapScreenForRestaurant(mapViewModel         : MapViewModel = hiltViewModel(),
                           restaurantId         : Int = -1,
                           cameraPositionState  : CameraPositionState = rememberCameraPositionState(),
                           selectedMarkerData   : MarkerData?,
                           mapUiSettings        : MapUiSettings = MapUiSettings(zoomControlsEnabled = true),
                           onMapClick           : (LatLng) -> Unit = {},
                           zoom                 : Float = 0f,
                           logoBottomPadding    : Dp = 0.dp) {
    val isMapLoaded = mapViewModel.uiState.isMapLoaded
    val coroutine = rememberCoroutineScope()
    //TODO:: restaurantId를 조회하여 마커 표시하기
    if(restaurantId > 0) {
        MapScreen(
            mapViewModel = mapViewModel,
            cameraPositionState = cameraPositionState,
            onMapClick = onMapClick,
            uiSettings = mapUiSettings,
            logoBottomPadding = logoBottomPadding,
            onMapLoaded = {
                coroutine.launch {
                    if (!isMapLoaded) { // 플래그 처리 안하면 지도화면으로 이동할때마다 이벤트 발생 처음에 한번만 동작하면 됨
                        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(LatLng(selectedMarkerData?.lat ?: 0.0, selectedMarkerData?.lon ?: 0.0), zoom), durationMs = 1000)
                        delay(1000)//카메라 이동 전까지 플래그 비활성화
                        mapViewModel.onMapLoaded()
                    }
                }
            }
        )
    }
    else {
        Text("잘못된 음식점 id. $restaurantId")
    }
}