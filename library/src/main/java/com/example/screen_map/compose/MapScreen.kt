package com.example.screen_map.compose

import android.Manifest
import android.content.Context
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
import com.example.screen_map.data.MapScreenCallback
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
import com.google.maps.android.compose.MarkerState
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
fun MapScreen(
    showLog                   : Boolean                                       = false,
    mapState                  : MapState                                      = rememberMapState(showLog = showLog),
    mapViewModel              : MapViewModel                                  = hiltViewModel(),
    onMark                    : (Int) -> Unit                                 = {},
    onMapClick                : (LatLng) -> Unit                              = {},
    uiSettings                : MapUiSettings                                 = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false, compassEnabled = false),
    onMapLoaded               : () -> Unit                                    = {},
    logoBottomPadding         : Dp                                            = 0.dp,
    markerDetailVisibleLevel  : Float                                         = 18f,
    content                   : @Composable @GoogleMapComposable () -> Unit   = { }
) {
    MapScreen_(
        uiState                     = mapViewModel.uiState,
        logoBottomPadding           = logoBottomPadding,
        mapState                    = mapState,
        uiSettings                  = uiSettings,
        showLog                     = showLog,
        markerDetailVisibleLevel    = markerDetailVisibleLevel,
        mapScreenCallback           = MapScreenCallback(
        onSaveCameraPosition        = { mapViewModel.saveCameraPosition(it) },
        onMapClick                  = onMapClick,
        onMapLoaded                 = { onMapLoaded(); mapViewModel.onMapLoaded() },
        onMark                      = { mapViewModel.onMark(it) },),
        content                     = content
    )
}

@Preview
@Composable
fun MapScreen_(
    tag                       : String                                      = "__MapScreen",
    showLog                   : Boolean                                     = false,
    mapState                  : MapState                                    = rememberMapState(showLog = showLog),
    uiState                   : MapUIState                                  = MapUIState(),
    logoBottomPadding         : Dp                                          = 0.dp,
    markerDetailVisibleLevel  : Float                                       = 18f,
    uiSettings                : MapUiSettings                               = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false, compassEnabled = false),
    mapScreenCallback         : MapScreenCallback                           = MapScreenCallback(),
    content                   : @Composable @GoogleMapComposable () -> Unit = { }
){
    val context             : Context       = LocalContext.current
    val isMyLocationEnabled : Boolean       = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    val mapProperties       : MapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = isMyLocationEnabled, mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.hide_all_type))) }

    Box {
        GoogleMap( modifier            = Modifier.fillMaxSize(),
                   cameraPositionState = mapState.cameraPositionState,
                   properties          = mapProperties,
                   onMapClick          = mapScreenCallback.onMapClick,
                   uiSettings          = uiSettings,
                   onMapLoaded         = mapScreenCallback.onMapLoaded,
                   contentPadding      = PaddingValues(bottom = logoBottomPadding))
        {

            Log.w(tag, "map recomposition")

            uiState.list
                .forEach { data ->
                        Marker(
                            tag     = data.id,
                            state   = data.markState(),
                            snippet = data.snippet,
                            onClick = {
                                mapScreenCallback.onMark(Integer.parseInt(it.tag.toString()))
                                false
                            },
                            icon    = data.icon(
                                context                 = context,
                                title                   = data.title,
                                rating                  = data.rating,
                                isSelected              = false,
                                price                   = data.price,
                                visibleTitle            = false,
                                visiblePriceAndRating   = false
                                //visibleTitle = mapState.zoomLevel > markerDetailVisibleLevel,
                                // visiblePriceAndRating = mapState.zoomLevel > markerDetailVisibleLevel
                            )
                        )
                }

            //TODO::select marker 표시 방법
            /*uiState.selectedMarker?.let {
                mapState.setSelectMarker(it)
                Marker( state   = mapState.selectedMarker,
                        snippet = it.snippet,
                        onClick = { false },
                        tag     = it.id,
                        icon    = it.icon( context     = context,
                                           title       = it.title,
                                           rating      = it.rating,
                                           isSelected  = true,
                                           price       = it.price))
            }*/
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

    if(showLog){
        LaunchedEffect(uiState) {
            showLog.log(tag, "recomposition : $uiState")
        }
    }

    // save location after point out finger
    LaunchedEffect(key1 = uiState.isMapLoaded, block = {
        snapshotFlow { mapState.cameraPositionState.isMoving }.collect {
            if (!mapState.cameraPositionState.isMoving) { // 맵이 로드될 때 0,0 좌표를 저장하는 이벤트 발생하여 방어로직추가
                showLog.log(tag, "isMoving changed: ${mapState.cameraPositionState.isMoving}")
                if (uiState.isMapLoaded) { //마지막으로 움직인 지점 저장하기
                    if(mapState.cameraPositionState.position.target.latitude != 0.0) // TODO::0.0 안나오게 하기
                    {
                        mapScreenCallback.onSaveCameraPosition(mapState.cameraPositionState)
                        showLog.log(tag, "좌표 저장 요청. ${mapState.cameraPositionState.position.target}")
                    }
                }else{
                    showLog.log(tag, "좌표 저장 안함. isMapLoaded is false : ${mapState.cameraPositionState.position.target}")
                }
            }
        }
    })
}

fun Boolean.log(tag : String, message : String) {
    if(this) Log.d(tag, message)
}