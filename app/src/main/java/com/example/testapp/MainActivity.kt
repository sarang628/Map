package com.example.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import com.example.screen_map.MapScreen
import com.example.screen_map.MapService
import com.example.screen_map.MapUiState
import com.example.screen_map.MapViewModel
import com.example.screen_map.MarkerData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
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

            Box {
                MapScreen(
                    mapViewModel = mapViewModel,
                    animationMoveDuration = 300,
                    onIdle = {},
                    cameraPositionState = cameraPositionState,
                    list = ArrayList(),
                    selectedMarkerData = null
                )

                Row {
                    Button(onClick = { }) {
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
                }
            }
        }
    }
}