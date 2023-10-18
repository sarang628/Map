package com.example.screen_map

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    onMark: ((Int) -> Unit)? = null,
    onIdle: () -> Unit,
    speed: Int = 300,
    cameraPositionState: CameraPositionState,
    list: List<MarkerData>?,
    selectedMarkerData: MarkerData?,
    currentLocation: Location? = null
) {
    val selectedMarker = rememberMarkerState().apply { showInfoWindow() }
    val myLocationMarker = rememberMarkerState().apply {}
    var isFirst by remember { mutableStateOf(true) }


    /* 맵에서 마커를 클릭 시 카드를 움직이게되는데
    이 때 카드가 움직이며 또 맵의 마커 이동 요청을 하게됨으로 이를 방지하기위해
    먼저 맵의 idle상태를 전달하기로 결정 */
    LaunchedEffect(key1 = cameraPositionState, block = {
        snapshotFlow { cameraPositionState.isMoving }.collect {
            if (!it) {
                Log.d("MapScreen", "idle")
                onIdle.invoke()
            }
        }
    })

    LaunchedEffect(key1 = selectedMarkerData) {
        selectedMarkerData?.let {
            if (selectedMarker.position != it.getLatLng()) {
                cameraPositionState.animate(
                    update = if (isFirst) CameraUpdateFactory.newLatLngZoom(
                        it.getLatLng(),
                        18f
                    ) else CameraUpdateFactory.newLatLng(it.getLatLng()),
                    durationMs = speed
                )
                isFirst = true
            }
        }
    }

    Box {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            selectedMarkerData?.let {
                selectedMarker.position = selectedMarkerData.getLatLng()
                Marker(
                    state = selectedMarker,
                    title = selectedMarkerData.title,
                    snippet = selectedMarkerData.snippet,
                    onClick = {
                        onMark?.invoke(Integer.parseInt(it.tag.toString()))
                        false
                    },
                    tag = selectedMarkerData.id,
                    icon = BitmapDescriptorFactory.fromResource(selectedMarkerData.icon)
                )
            }

            if (currentLocation != null) {
                myLocationMarker.position =
                    LatLng(currentLocation.latitude, currentLocation.longitude)
                Marker(
                    state = myLocationMarker,
                    onClick = {
                        false
                    },
                    icon = BitmapDescriptorFactory.defaultMarker()
                )
            }

            list?.let {
                for (data: MarkerData in it) {
                    Marker(
                        state = data.markState(),
                        title = data.title,
                        snippet = data.snippet,
                        onClick = {
                            onMark?.invoke(Integer.parseInt(it.tag.toString()))
                            false
                        },
                        tag = data.id,
                        icon = BitmapDescriptorFactory.fromResource(data.icon)
                    )
                }
            }
        }
    }
}

private class MyLocationSource : LocationSource {

    private var listener: LocationSource.OnLocationChangedListener? = null

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        this.listener = listener
    }

    override fun deactivate() {
        listener = null
    }

    fun onLocationChanged(location: Location) {
        listener?.onLocationChanged(location)
    }
}

private fun newLocation(): Location {
    val location = Location("MyLocationProvider")
    return location
}