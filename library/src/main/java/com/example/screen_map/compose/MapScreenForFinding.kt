package com.example.screen_map.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.data.MapScreenCallback
import com.example.screen_map.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private const val tag : String = "__MapScreenForFinding"

/**
 * @param mapViewModel map 뷰모델
 * @param onMark map 마커 클릭 이벤트
 * @param cameraSpeed map 카메라 이동 속도 설정
 * @param onMapClick 맵 클릭 이벤트
 * @param myLocation 내 위치로 이동
 * @param boundary 내 위치 반경 표시
 */
@Composable
fun MapScreenForFinding(mapViewModel       : MapViewModel          = hiltViewModel(),
                        cameraSpeed        : Int                   = 300,
                        mapState           : MapState              = rememberMapState(),
                        onMapClick         : (LatLng) -> Unit      = {},
                        myLocation         : LatLng?               = null,
                        boundary           : Double?               = null,
                        logoBottomPadding  : Dp                    = 0.dp,
                        onMark             : (Int) -> Unit         = {},
                        uiSettings         : MapUiSettings         = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false, compassEnabled = false), ) {
    val coroutine = rememberCoroutineScope()

    MapForFinding_(
        uiState                     = mapViewModel.uiState,
        mapState                    = mapState,
        onMapClick                  = onMapClick,
        myLocation                  = myLocation,
        boundary                    = boundary,
        logoBottomPadding           = logoBottomPadding,
        uiSettings                  = uiSettings,
        onSaveCameraPosition        = { mapViewModel.saveCameraPosition(it) },
        onMark                      = { mapViewModel.onMark(it) },
        onMapLoaded                 = {
            coroutine.launch {
                mapViewModel.onMapLoaded()
                if (!mapViewModel.uiState.isMapLoaded) { // 플래그 처리 안하면 지도화면으로 이동할때마다 이벤트 발생 처음에 한번만 동작하면 됨
                    mapState.cameraPositionState.animate(update = CameraUpdateFactory.newLatLngZoom(mapViewModel.getLastPosition(),
                                                                                                    mapViewModel.getLastZoom()),
                                                         durationMs = 1000)
                    delay(1000) //카메라 이동 전까지 플래그 비활성화
                }
            }
        }
    )
}

@Preview
@Composable
fun MapForFinding_(uiState                     : MapUIState            = MapUIState(),
                   mapState                    : MapState              = rememberMapState(),
                   onMapClick                  : (LatLng) -> Unit      = {},
                   myLocation                  : LatLng?               = null,
                   boundary                    : Double?               = null,
                   logoBottomPadding           : Dp                    = 0.dp,
                   onMark                      : (Int) -> Unit         = {},
                   onMapLoaded                 : () -> Unit            = {},
                   onSaveCameraPosition        : (CameraPositionState) -> Unit = {},
                   uiSettings                  : MapUiSettings         = MapUiSettings(zoomControlsEnabled = false,
                                                                                       myLocationButtonEnabled = false,
                                                                                       compassEnabled = false), ){

    var zoom by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        snapshotFlow { mapState.cameraPositionState.position.zoom.toInt() }
            .distinctUntilChanged()
            .collect { zoom = it }
    }

    LaunchedEffect(uiState.cameraPosition) {
        uiState.cameraPosition?.let {
            mapState.cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(LatLng(it.first, it.second),  it.third),
                durationMs = 300,
            )
        }
    }

    Map(showProgress      = uiState.isMapLoaded,
        mapState          = mapState,
        mapScreenCallback = MapScreenCallback(
            onSaveCameraPosition = onSaveCameraPosition,
            onMapClick           = onMapClick,
            onMapLoaded          = onMapLoaded),
        logoBottomPadding = logoBottomPadding,
        uiSettings        = uiSettings){
        Markers(list        = uiState.list,
                onMark      = { onMark.invoke(it) },
                visibleInfo = zoom  >= 17)

        myLocation?.let { latlng ->
            boundary?.let {
                Circle(center = latlng, radius = boundary, strokeWidth = 5f, strokeColor = MaterialTheme.colorScheme.primary)
            }
        }

        SelectedMarker(uiState.selectedMarker)
    }
}

@Preview
@Composable
fun test(){
    MapForFinding_(
    )
}