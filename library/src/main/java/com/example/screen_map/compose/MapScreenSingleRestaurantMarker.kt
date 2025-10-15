package com.example.screen_map.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun MapScreenSingleRestaurantMarker(mapViewModel         : MapViewModel = hiltViewModel(),
                                    restaurantId         : Int          = -1,
                                    mapState             : MapState     = rememberMapState(),
                                    selectedMarkerData   : MarkerData?  = null,
                                    mapUiSettings        : MapUiSettings = MapUiSettings(zoomControlsEnabled = true),
                                    onMapClick           : (LatLng) -> Unit = {},
                                    zoom                 : Float = 17f,
                                    logoBottomPadding    : Dp = 0.dp) {
    val isMapLoaded = mapViewModel.uiState.isMapLoaded
    val selectedMarker = mapViewModel.selectedMarker
    val coroutine = rememberCoroutineScope()
    val uiState = mapViewModel.uiState
    //TODO:: restaurantId를 조회하여 마커 표시하기

    LaunchedEffect(selectedMarker != null && isMapLoaded) {
        mapState.cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(LatLng(selectedMarker?.lat ?:0.0, selectedMarker?.lon ?: 0.0 ), zoom), durationMs = 300)
    }

    if(restaurantId > 0) {
        MapScreen(
            mapViewModel = mapViewModel,
            onMapClick = onMapClick,
            uiSettings = mapUiSettings,
            logoBottomPadding = logoBottomPadding,
            onMapLoaded = {
                mapViewModel.findRestaurant(restaurantId)
            }
        )
    }
    else {
        Text("잘못된 음식점 id. $restaurantId")
    }
}