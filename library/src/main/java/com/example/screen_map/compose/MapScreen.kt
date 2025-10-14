package com.example.screen_map.compose

import android.Manifest
import android.content.pm.PackageManager
import android.nfc.Tag
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.icon
import com.example.screen_map.viewmodels.MapUIState
import com.example.screen_map.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.sarang.torang.R

/**
 * @param mapViewModel map 뷰모델
 * @param onMark map 마커 클릭 이벤트
 * @param cameraPositionState map 카메라 위치 상태 객체
 * @param selectedMarkerData 선택된 마커. 외부에서 마커로 위치시키고 싶을 때 사용
 * @param onMapClick 맵 클릭 이벤트
 */
@Composable
fun MapScreen(mapViewModel              : MapViewModel = hiltViewModel(),
              onMark                    : (Int) -> Unit = {},
              cameraPositionState       : CameraPositionState = rememberCameraPositionState(),
              onMapClick                : (LatLng) -> Unit = {},
              uiSettings                : MapUiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false, compassEnabled = false),
              onMapLoaded               : () -> Unit = {},
              logoBottomPadding         : Dp = 0.dp,
              markerDetailVisibleLevel  : Float = 18f,
              showLog                   : Boolean                                     = false,
              content                   : @Composable @GoogleMapComposable () -> Unit = { }) {

    MapScreen_(
        cameraPositionState = cameraPositionState,
        uiState = mapViewModel.uiState,
        logoBottomPadding = logoBottomPadding,
        uiSettings = uiSettings,
        showLog = showLog,
        markerDetailVisibleLevel = markerDetailVisibleLevel,
        mapScreenCallback = MapScreenCallback(
        onSaveCameraPosition = { mapViewModel.saveCameraPosition(it) },
        onMapClick = onMapClick,
        onMapLoaded = { onMapLoaded(); mapViewModel.onMapLoaded() },
        onMark = { mapViewModel.onMark(it) },),
        content = content
    )
}

data class MapScreenCallback(
    val onSaveCameraPosition      : (CameraPositionState) -> Unit               = {},
    val onMapClick                : (LatLng) -> Unit                            = {},
    val onMapLoaded               : () -> Unit                                  = {},
    val onMark                    : (Int) -> Unit                               = {},
)

@Preview
@Composable
fun MapScreen_(
    tag                       : String                                      = "__MapScreen",
    cameraPositionState       : CameraPositionState                         = rememberCameraPositionState(),
    uiState                   : MapUIState                                  = MapUIState(),
    logoBottomPadding         : Dp                                          = 0.dp,
    showLog                   : Boolean                                     = false,
    markerDetailVisibleLevel  : Float                                       = 18f,
    uiSettings                : MapUiSettings                               = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false, compassEnabled = false),
    mapScreenCallback         : MapScreenCallback                           = MapScreenCallback(),
    content                   : @Composable @GoogleMapComposable () -> Unit = { }
){
    val context = LocalContext.current
    val selectedMarker = rememberMarkerState().apply { showInfoWindow() }
    val isMyLocationEnabled = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = isMyLocationEnabled, mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.hide_all_type))) }
    var zoomLevel by remember { mutableFloatStateOf(cameraPositionState.position.zoom) } // 카메라의 줌 레벨을 추적

    if(showLog){
        LaunchedEffect(uiState) {
            showLog.log(tag, "recomposition : $uiState")
        }
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            zoomLevel = cameraPositionState.position.zoom
            showLog.log(tag, "zoomLevel:$zoomLevel")
        }
    }

    LaunchedEffect(key1 = cameraPositionState, block = {
        snapshotFlow { cameraPositionState.isMoving }.collect {
            if (!cameraPositionState.isMoving) { // 맵이 로드될 때 0,0 좌표를 저장하는 이벤트 발생하여 방어로직추가
                showLog.log(tag, "isMoving:${cameraPositionState.isMoving}")
                if (uiState.isMapLoaded) { //마지막으로 움직인 지점 저장하기
                    mapScreenCallback.onSaveCameraPosition(cameraPositionState)
                    showLog.log(tag, "좌표 저장 요청. onSaveCameraPosition:$cameraPositionState")
                }else{
                    showLog.log(tag, "좌표 저장 않함. isMapLoaded false. : ${cameraPositionState.position}")
                }
            }
        }
    })

    LaunchedEffect(key1 = uiState.selectedMarker) {
        if (!uiState.isMapLoaded) return@LaunchedEffect

        uiState.selectedMarker?.let { //카드가 포커스된 음식점에 맞춰 지도 이동시키기
            if (selectedMarker.position != it.getLatLng()) {
                cameraPositionState.animate(update = CameraUpdateFactory.newLatLng(it.getLatLng()), durationMs = 300)
            }
        }
    }

    Box {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            onMapClick = mapScreenCallback.onMapClick,
            uiSettings = uiSettings,
            onMapLoaded = { mapScreenCallback.onMapLoaded.invoke() },
            contentPadding = PaddingValues(bottom = logoBottomPadding)) {
            uiState.list.let {
                for (data: MarkerData in it) {
                    if(uiState.selectedMarker?.title != data.title)
                        Marker(tag = data.id, state = data.markState(), /*title = data.title,*/ snippet = data.snippet, onClick = { mapScreenCallback.onMark(Integer.parseInt(it.tag.toString())); false }, icon = data.icon(context, data.title, data.rating, false, data.price, zoomLevel > markerDetailVisibleLevel, zoomLevel > markerDetailVisibleLevel))
                }
            }
            uiState.selectedMarker?.let {
                selectedMarker.position = it.getLatLng()
                Marker(state = selectedMarker, /*title = it.title,*/ snippet = it.snippet, onClick = { false }, tag = it.id, icon = it.icon(context, it.title, it.rating, isSelected = true, it.price))
            }
            content.invoke()
        }
        if (!uiState.isMapLoaded) {
            Box(Modifier
                .fillMaxSize()
                .clickable(enabled = false) { }
                .background(Color(0x33000000))) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}

fun Boolean.log(tag : String, message : String) {
    if(this) Log.d(tag, message)
}