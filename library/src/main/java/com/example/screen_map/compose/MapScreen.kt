package com.example.screen_map.compose

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.data.MapScreenCallback
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.icon
import com.example.screen_map.viewmodels.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.sarang.torang.R
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.tasks.await

private const val tag : String = "__MapScreen"

/**
 * @param mapViewModel map 뷰모델
 * @param onMark map 마커 클릭 이벤트
 * @param onMapClick 맵 클릭 이벤트
 */
@Composable
fun MapScreen(mapState                      : MapState              = rememberMapState(),
              mapViewModel                  : MapViewModel          = hiltViewModel(),
              onMark                        : (Int) -> Unit         = {},
              onMapClick                    : (LatLng) -> Unit      = {},
              uiSettings                    : MapUiSettings         = MapUiSettings(zoomControlsEnabled = false,
                                                                                    myLocationButtonEnabled = false,
                                                                                    compassEnabled = false),
              onMapLoaded                   : () -> Unit            = {},
              logoBottomPadding             : Dp                    = 0.dp,
              markerTitleVisibleZoomLevel   : Float                 = 17f,
              mapProperties                 : MapProperties         = MapProperties(isMyLocationEnabled = true,
                                                                                    mapStyleOptions     = MapStyleOptions.loadRawResourceStyle(LocalContext.current, R.raw.hide_all_type)),
              content                       : @Composable
                                              @GoogleMapComposable
                                              () -> Unit            = { }) {
    val uiState = mapViewModel.uiState
    var zoom by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val locationClient : FusedLocationProviderClient   = remember { LocationServices.getFusedLocationProviderClient(context) }

    LaunchedEffect(Unit) {
        snapshotFlow { mapState.cameraPositionState.position.zoom.toInt() }
            .distinctUntilChanged()
            .collect { zoom = it }
    }

    LaunchedEffect(uiState.cameraPosition) {
        uiState.cameraPosition?.let {
            mapState.cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(LatLng(it.first,
                                                                  it.second),
                                                           it.third),
                durationMs = 300,
            )
        }
    }

    LaunchedEffect(uiState.findMyLocation) {
        if(uiState.findMyLocation){
            val grant = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            if(grant == PackageManager.PERMISSION_GRANTED){
                val result : Location? = locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token,)
                    .await()
                result?.let {
                    mapState.cameraPositionState.animate(CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude)))
                }
                mapViewModel.onFindMyLocation()
            }
            else{
                mapViewModel.onFindMyLocation()
            }
        }
    }

    Map(loadingProgress   = uiState.isMapLoaded,
        showProgress      = uiState.findMyLocation,
        logoBottomPadding = logoBottomPadding,
        mapState          = mapState,
        uiSettings        = uiSettings,
        mapProperties     = mapProperties,
        mapScreenCallback = MapScreenCallback(
            onSaveCameraPosition  = { mapViewModel.saveCameraPosition(it) },
            onMapClick            = onMapClick,
            onMapLoaded           = { onMapLoaded(); mapViewModel.onMapLoaded() })
    ){
        Markers(list        = uiState.markers,
                onMark      = { mapViewModel.onMark(it) },
                visibleInfo = zoom  >= markerTitleVisibleZoomLevel)
        content()
        SelectedMarker(uiState.selectedMarker)
    }
}



@Composable
@GoogleMapComposable
internal fun SelectedMarker(
    selectedMarker : MarkerData?
) {
    selectedMarker?.let {
        Marker( state   = it.markState(),
            snippet = it.snippet,
            onClick = { false },
            tag     = it.id,
            icon    = it.icon(context     = LocalContext.current,
                              title       = it.title,
                              rating      = it.rating,
                              isSelected  = true,
                              price       = it.price,
                              visibleTitle = true,
                              visiblePriceAndRating = true))
    }
}