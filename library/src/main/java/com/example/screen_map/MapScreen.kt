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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlin.random.Random

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
    val context = LocalContext.current
    val selectedMarker = rememberMarkerState().apply {
        showInfoWindow()
    }
    val TAG = "MapScreen"
    val zoom = 18f

    val locationSource = MyLocationSource()
    // Collect location updates
    var locationState by remember { mutableStateOf(newLocation()) }

    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }

    // 맵에서 마커를 클릭 시 카드를 움직이게되는데
    // 이 때 카드가 움직이며 또 맵의 마커 이동 요청을 하게됨으로 이를 방지하기위해
    // 먼저 맵의 idle상태를 전달하기로 결정
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
                    update = CameraUpdateFactory.newLatLng(it.getLatLng()),
                    speed
                )
            }
        }
    }

    // Update blue dot and camera when the location changes
    currentLocation?.let {
        LaunchedEffect(currentLocation) {
            Log.d(TAG, "Updating blue dot on map...")
            locationSource.onLocationChanged(it)

            Log.d(TAG, "Updating camera position...")
            val cameraPosition = CameraPosition.fromLatLngZoom(
                LatLng(
                    it.latitude,
                    it.longitude
                ), zoom
            )
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                300
            )
        }
    }


    // Detect when the map starts moving and print the reason
    LaunchedEffect(cameraPositionState.isMoving) {
        if (cameraPositionState.isMoving) {
            Log.d(
                TAG,
                "Map camera started moving due to ${cameraPositionState.cameraMoveStartedReason.name}"
            )
        }
    }

    Box {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            locationSource = locationSource
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