package com.example.screen_map.compose

import android.util.Log
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.data.MapScreenCallback
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.icon
import com.example.screen_map.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private const val tag : String = "__MapScreen"

/**
 * @param mapViewModel map 뷰모델
 * @param onMark map 마커 클릭 이벤트
 * @param onMapClick 맵 클릭 이벤트
 */
@Composable
fun MapScreen(mapState                  : MapState                                      = rememberMapState(),
              mapViewModel              : MapViewModel                                  = hiltViewModel(),
              onMark                    : (Int) -> Unit                                 = {},
              onMapClick                : (LatLng) -> Unit                              = {},
              uiSettings                : MapUiSettings                                 = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false, compassEnabled = false),
              onMapLoaded               : () -> Unit                                    = {},
              logoBottomPadding         : Dp                                            = 0.dp,
              content                   : @Composable @GoogleMapComposable () -> Unit   = { }) {
    val uiState = mapViewModel.uiState
    var zoom by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        snapshotFlow { mapState.cameraPositionState.position.zoom.toInt() }
            .distinctUntilChanged()
            .collect { zoom = it }
    }

    LaunchedEffect(uiState.cameraPosition) {
        uiState.cameraPosition?.let {
            mapState.cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(LatLng(it.first, it.second), it.third),
                durationMs = 300,
            )
        }
    }

    Map(showProgress      = uiState.isMapLoaded,
        logoBottomPadding = logoBottomPadding,
        mapState          = mapState,
        uiSettings        = uiSettings,
        mapScreenCallback = MapScreenCallback(
            onSaveCameraPosition  = { mapViewModel.saveCameraPosition(it) },
            onMapClick            = onMapClick,
            onMapLoaded           = { onMapLoaded(); mapViewModel.onMapLoaded() })
    ){
        Markers(list        = uiState.list,
                onMark      = { mapViewModel.onMark(it) },
                visibleInfo = zoom  >= 17)
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