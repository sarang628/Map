package com.example.testapp

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.screen_map.compose.CurrentLocationScreen
import com.example.screen_map.compose.MapScreen
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.testMarkArrayList
import com.google.android.gms.maps.CameraUpdateFactory.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.samples.apps.sunflower.ui.TorangTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TorangTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    val coroutineScope = rememberCoroutineScope()
                    val cameraPositionState = rememberCameraPositionState()
                    var selectedMarkerData: MarkerData? by remember { mutableStateOf(null) }
                    val list = testMarkArrayList()
                    var location: Location? by remember { mutableStateOf(null) }
                    var isMyLocationEnabled by remember { mutableStateOf(false) }
                    var myLocation: LatLng? by remember { mutableStateOf(null) }
                    val navHostController = rememberNavController()
                    var boundary by remember { mutableStateOf(0.0) }

                    Box {
                        NavHost(navController = navHostController, startDestination = "map") {
                            composable("map") {
                                MapScreen(
                                    cameraPositionState = cameraPositionState,
                                    list = list,
                                    selectedMarkerData = selectedMarkerData,
                                    myLocation = myLocation,
                                    boundary = boundary
                                )
                            }
                            composable("restaurant") {

                            }

                        }


                        Column(
                            Modifier
                                .scrollable(
                                    rememberScrollState(),
                                    orientation = Orientation.Horizontal
                                )) {
                            Button(onClick = {
                                selectedMarkerData = list[Random.nextInt(list.size - 1)]
                            }) { Text(text = "pageChange") }
                            Button(onClick = { coroutineScope.launch { cameraPositionState.animate(zoomIn()) } }) {
                                Text(
                                    text = "+"
                                )
                            }
                            Button(onClick = { coroutineScope.launch { cameraPositionState.animate(zoomOut()) } }) {
                                Text(
                                    text = "-"
                                )
                            }
                            CurrentLocationScreen(onLocation = {
                                location = it
                                isMyLocationEnabled = !isMyLocationEnabled; coroutineScope.launch {
                                cameraPositionState.animate(
                                    update = newLatLng(
                                        LatLng(it.latitude, it.longitude)
                                    ), 300
                                )
                                myLocation = LatLng(it.latitude, it.longitude)
                            }
                            })
                            Button(onClick = { navHostController.navigate("restaurant") }) { Text(text = "aa") }
                            Button(onClick = { boundary = 100.0 }) { Text(text = "100M") }
                            Button(onClick = { boundary = 200.0 }) { Text(text = "200M") }
                            Button(onClick = { boundary = 500.0 }) { Text(text = "500M") }
                            Button(onClick = { boundary = 1000.0 }) { Text(text = "1000M") }
                            Button(onClick = { boundary = 3000.0 }) { Text(text = "3000M") }
                        }
                    }

                }
            }
        }
    }
}

private fun newLocation(): Location {
    val location = Location("MyLocationProvider")
    return location
}