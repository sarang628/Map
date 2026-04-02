package com.example.screen_map.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.data.MapScreenCallback
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.icon
import com.example.screen_map.viewmodels.MapSingleMarkerViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker

/**
 * @param mapViewModel map 뷰모델
 * @param selectedMarkerData 선택된 마커. 외부에서 마커로 위치시키고 싶을 때 사용
 * @param onMapClick 맵 클릭 이벤트
 */
@Composable
fun MapScreenSingleRestaurantMarker(mapViewModel         : MapSingleMarkerViewModel = hiltViewModel(),
                                    restaurantId         : Int          = -1,
                                    mapState             : MapState     = rememberMapState(),
                                    selectedMarkerData   : MarkerData?  = null,
                                    mapUiSettings        : MapUiSettings = MapUiSettings(zoomControlsEnabled = true,
                                                                                         compassEnabled = true,
                                                                                         myLocationButtonEnabled = true),
                                    onMapClick           : (LatLng) -> Unit = {},
                                    zoom                 : Float = 17f,
                                    logoBottomPadding    : Dp = 0.dp) {
    val uiState = mapViewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(uiState.selectedMarker, uiState.isMapLoaded) {
        uiState.selectedMarker?.let {
            if(uiState.isMapLoaded)
                mapState.cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngZoom(LatLng(it.lat,
                                                                     it.lon), zoom),
                    durationMs = 300)
        }
    }


    LaunchedEffect(restaurantId, uiState.isMapLoaded) {
        mapViewModel.selectRestaurant(restaurantId)
    }

    if(restaurantId > 0) {
        Map(mapState = mapState,
            isMapLoaded = mapViewModel.uiState.isMapLoaded,
            uiSettings = mapUiSettings,
            logoBottomPadding = logoBottomPadding,
            mapScreenCallback = MapScreenCallback(
                onMapLoaded = { mapViewModel.onMapLoaded() },
                onMapClick = onMapClick
            )
        ){
            mapViewModel.uiState.selectedMarker?.let {
                Marker( state   = it.markState(),
                    snippet = it.snippet,
                    onClick = { false },
                    tag     = it.id,
                    icon    = it.icon(
                        context     = context,
                        title       = it.title,
                        rating      = it.rating,
                        isSelected  = true,
                        price       = it.price))
            }
        }
    }
    else {
        Text("잘못된 음식점 id. $restaurantId")
    }
}