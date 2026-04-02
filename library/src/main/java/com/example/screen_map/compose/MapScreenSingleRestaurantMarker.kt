package com.example.screen_map.compose

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.data.MapScreenCallback
import com.example.screen_map.data.MarkerData
import com.example.screen_map.viewmodels.MapSingleMarkerViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng

private val tag = "__MapScreenSingleRestaurantMarker"

/**
 * @param mapViewModel map 뷰모델
 * @param selectedMarkerData 선택된 마커. 외부에서 마커로 위치시키고 싶을 때 사용
 * @param onMapClick 맵 클릭 이벤트
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenSingleRestaurantMarker(mapViewModel         : MapSingleMarkerViewModel = hiltViewModel(),
                                    restaurantId         : Int          = -1,
                                    mapState             : MapState     = rememberMapState(),
                                    selectedMarkerData   : MarkerData?  = null,
                                    onMapClick           : (LatLng) -> Unit = {},
                                    zoom                 : Float = 17f,
                                    logoBottomPadding    : Dp = 0.dp,
                                    requestPermission    : () -> Unit = {},
                                    onBack               : () -> Unit = {},
                                    hasPermission        : Boolean    = false) {
    val uiState = mapViewModel.uiState

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

    Scaffold(topBar = {
        TopAppBar(
            { Text(uiState.selectedMarker?.title ?: "", fontSize = 20.sp) },
            navigationIcon = {
                IconButton(onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            })
    },
        floatingActionButton = {
            if(!hasPermission)
                FloatingActionButton({ if(!hasPermission) requestPermission() }) {
                Icon(Icons.Default.LocationOn, contentDescription = null) }
        }
    ) {
        Box(Modifier.padding(it)){
            if(restaurantId > 0) {
                Map(mapState    = mapState,
                    showProgress = uiState.isMapLoaded,
                    myLocationButtonEnabled = hasPermission, //  이 두 파라미터가 true로 설정되어야 지도 내위치 버튼 활성화
                    isMyLocationEnabled = hasPermission,     //  이 두 파라미터가 true로 설정되어야 지도 내위치 버튼 활성화
                    logoBottomPadding = logoBottomPadding,
                    mapScreenCallback = MapScreenCallback(
                        onMapLoaded = { mapViewModel.onMapLoaded() },
                        onMapClick = onMapClick
                    )
                ){
                    uiState.selectedMarker?.let {
                        Marker(it, visibleInfo = true)
                    }
                }
            }
            else {
                Text("잘못된 음식점 id. $restaurantId")
            }
        }
    }
}