package com.example.screen_map.compose

import android.Manifest
import android.content.Context
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.screen_map.data.MapScreenCallback
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.sarang.torang.R

@Preview
@Composable
fun Map(
    mapState                  : MapState                                    = rememberMapState(),
    isMapLoaded               : Boolean                                     = false,
    logoBottomPadding         : Dp                                          = 0.dp,
    uiSettings                : MapUiSettings                               = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false, compassEnabled = false),
    mapScreenCallback         : MapScreenCallback                           = MapScreenCallback(),
    content                   : @Composable @GoogleMapComposable () -> Unit = { }
){
    val context             : Context       = LocalContext.current
    val isMyLocationEnabled : Boolean       = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    val mapProperties       : MapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = isMyLocationEnabled, mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.hide_all_type))) }

    Box {
        GoogleMap(modifier            = Modifier.fillMaxSize(),
                  cameraPositionState = mapState.cameraPositionState,
                  properties          = mapProperties,
                  onMapClick          = mapScreenCallback.onMapClick,
                  uiSettings          = uiSettings,
                  onMapLoaded         = mapScreenCallback.onMapLoaded,
                  contentPadding      = PaddingValues(bottom = logoBottomPadding))
        {
            content.invoke()
        }
        if (!isMapLoaded) {
            Box(Modifier
                .fillMaxSize()
                .clickable(enabled = false) { }
                .background(Color(0x33000000))) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }

    // save location after point out finger
    LaunchedEffect(key1 = isMapLoaded, block = {
        snapshotFlow { mapState.cameraPositionState.isMoving }.collect {
            if (!mapState.cameraPositionState.isMoving) { // 맵이 로드될 때 0,0 좌표를 저장하는 이벤트 발생하여 방어로직추가
                if (isMapLoaded) { //마지막으로 움직인 지점 저장하기
                    if(mapState.cameraPositionState.position.target.latitude != 0.0) // TODO::0.0 안나오게 하기
                    {
                        mapScreenCallback.onSaveCameraPosition(mapState.cameraPositionState)
                    }
                }
            }
        }
    })
}