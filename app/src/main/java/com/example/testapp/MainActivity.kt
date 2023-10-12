package com.example.testapp

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.screen_map.CurrentLocationScreen
import com.example.screen_map.MapScreen
import com.example.screen_map.MapService
import com.example.screen_map.MapViewModel
import com.example.screen_map.MarkerData
import com.example.screen_map.testMarkArrayList
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mapViewModel: MapViewModel by viewModels()

    @Inject
    lateinit var mapService: MapService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val coroutineScope = rememberCoroutineScope()
            val cameraPositionState = rememberCameraPositionState()
            var selectedMarkerData: MarkerData? by remember { mutableStateOf(null) }
            val list = testMarkArrayList()
            var location by remember { mutableStateOf(newLocation()) }

            Box {
                MapScreen(
                    mapViewModel = mapViewModel,
                    onIdle = {},
                    cameraPositionState = cameraPositionState,
                    list = list,
                    selectedMarkerData = selectedMarkerData,
                    currentLocation = location
                )

                Row {
                    Button(onClick = {
                        selectedMarkerData = list[Random.nextInt(list.size - 1)]
                    }) {
                        Text(text = "pageChange")
                    }

                    Button(onClick = {
                        coroutineScope.launch {
                            cameraPositionState.animate(CameraUpdateFactory.zoomIn())
                        }
                    }) {
                        Text(text = "+")
                    }
                    Button(onClick = {
                        coroutineScope.launch {
                            cameraPositionState.animate(CameraUpdateFactory.zoomOut())
                        }
                    }) {
                        Text(text = "-")
                    }

                    CurrentLocationScreen(onLocation = {
                        location = it
                    })
                }
            }
        }
    }
}

private fun newLocation(): Location {
    val location = Location("MyLocationProvider")
    return location
}