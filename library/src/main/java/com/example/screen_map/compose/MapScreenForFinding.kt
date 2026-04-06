package com.example.screen_map.compose

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.data.MapScreenCallback
import com.example.screen_map.viewmodels.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.sarang.torang.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val tag : String = "__MapScreenForFinding"

/**
 * @param mapViewModel map 뷰모델
 * @param onMark map 마커 클릭 이벤트
 * @param cameraSpeed map 카메라 이동 속도 설정
 * @param onMapClick 맵 클릭 이벤트
 * @param myLocation 내 위치로 이동
 */
@Composable
fun MapScreenForFinding(mapViewModel       : MapViewModel          = hiltViewModel(),
                        cameraSpeed        : Int                   = 300,
                        mapState           : MapState              = rememberMapState(),
                        onMapClick         : (LatLng) -> Unit      = {},
                        logoBottomPadding  : Dp                    = 0.dp,
                        markerTitleVisibleZoomLevel   : Float      = 17f,
                        onMark             : (Int) -> Unit         = {},
                        mapProperties      : MapProperties         = MapProperties(isMyLocationEnabled = true,
                                                                                   mapStyleOptions     = MapStyleOptions.loadRawResourceStyle(LocalContext.current, R.raw.hide_all_type)),
                        uiSettings         : MapUiSettings         = MapUiSettings(zoomControlsEnabled      = false,
                                                                                   myLocationButtonEnabled  = false,
                                                                                   compassEnabled           = false)) {
    val uiState     : MapUIState        = mapViewModel.uiState
    val coroutine   : CoroutineScope    = rememberCoroutineScope()
    val context     : Context           = LocalContext.current
    var myLocation  : LatLng? by remember { mutableStateOf(null) }
    val locationClient : FusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    LaunchedEffect(uiState.findMyLocation) {
        if(uiState.findMyLocation){
            val grant = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            if(grant == PackageManager.PERMISSION_GRANTED){
                val result : Location? = locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
                                                                           CancellationTokenSource().token,)
                                                       .await()
                result?.let {
                    mapState.cameraPositionState.animate(CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude)))
                    myLocation = LatLng(it.latitude, it.longitude)
                }
                mapViewModel.onFindMyLocation()
            }
            else{
                mapViewModel.onFindMyLocation()
            }
        }
    }

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

    MapForFinding_(uiState                     = uiState,
                   mapState                    = mapState,
                   onMapClick                  = onMapClick,
                   myLocation                  = myLocation,
                   mapProperties               = mapProperties,
                   logoBottomPadding           = logoBottomPadding,
                   uiSettings                  = uiSettings,
                   onSaveCameraPosition        = { mapViewModel.saveCameraPosition(it) },
                   markerTitleVisibleZoomLevel = markerTitleVisibleZoomLevel,
                   onMark                      = { mapViewModel.onMark(it) },
                   zoom                        = zoom,
                   onMapLoaded                 = {
                        coroutine.launch {
                            mapViewModel.onMapLoaded()
                            if (!uiState.isMapLoaded) { // 플래그 처리 안하면 지도화면으로 이동할때마다 이벤트 발생 처음에 한번만 동작하면 됨
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
                   logoBottomPadding           : Dp                    = 0.dp,
                   mapProperties               : MapProperties         = MapProperties(isMyLocationEnabled = true,
                                                                                       mapStyleOptions     = MapStyleOptions.loadRawResourceStyle(LocalContext.current, R.raw.hide_all_type)),
                   onMark                      : (Int) -> Unit         = {},
                   onMapLoaded                 : () -> Unit            = {},
                   onSaveCameraPosition        : (CameraPositionState) -> Unit = {},
                   markerTitleVisibleZoomLevel : Float                 = 17f,
                   zoom                        : Int                   = 0,
                   uiSettings                  : MapUiSettings         = MapUiSettings(zoomControlsEnabled = false,
                                                                                       myLocationButtonEnabled = false,
                                                                                       compassEnabled = false), ){



    Map(loadingProgress      = uiState.isMapLoaded,
        showProgress         = uiState.findMyLocation,
        mapState             = mapState,
        mapScreenCallback    = MapScreenCallback(onSaveCameraPosition = onSaveCameraPosition,
                                                 onMapClick           = onMapClick,
                                                 onMapLoaded          = onMapLoaded),
        logoBottomPadding    = logoBottomPadding,
        mapProperties        = mapProperties,
        uiSettings           = uiSettings){
        Markers(list        = uiState.markers,
                onMark      = { onMark.invoke(it) },
                visibleInfo = zoom  >= markerTitleVisibleZoomLevel)

        myLocation?.let { latlng ->
            uiState.boundary?.let {
                Circle(center       = latlng,
                       radius       = uiState.boundary,
                       strokeWidth  = 5f,
                       strokeColor  = MaterialTheme.colorScheme.primary)
            }
        }

        SelectedMarker(uiState.selectedMarker)
    }
}

@Preview
@Composable
fun test(){
    MapForFinding_(/*Preview*/
    )
}