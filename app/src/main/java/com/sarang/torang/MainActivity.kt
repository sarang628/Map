package com.sarang.torang

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.screen_map.compose.CurrentLocationScreen
import com.example.screen_map.compose.MapScreen
import com.example.screen_map.compose.MapScreenForFinding
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.testMarkArrayList
import com.example.screen_map.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.sarang.torang.data.Filter
import com.sarang.torang.repository.FindRepository
import com.sryang.torang.ui.TorangTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var findRepository : FindRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TorangTheme {
                Surface(Modifier.Companion.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MapTest()
                }
            }
        }
    }

    @Composable
    fun MapTest(){
        val coroutineScope = rememberCoroutineScope()
        val cameraPositionState = rememberCameraPositionState()
        var selectedMarkerData: MarkerData? by remember { mutableStateOf(null) }
        val list = testMarkArrayList()
        var location: Location? by remember { mutableStateOf(null) }
        var isMyLocationEnabled by remember { mutableStateOf(false) }
        var myLocation: LatLng? by remember { mutableStateOf(null) }
        val navHostController = rememberNavController()
        var boundary by remember { mutableStateOf(0.0) }
        val mapViewModel: MapViewModel = hiltViewModel()

        Box {
            NavHost(navController = navHostController, startDestination = "map") {
                composable("map") {
                    //MapScreen(mapViewModel = mapViewModel, cameraPositionState = cameraPositionState, selectedMarkerData = selectedMarkerData)
                    MapScreenForFinding(mapViewModel = mapViewModel, cameraPositionState = cameraPositionState, selectedMarkerData = selectedMarkerData)
                }
                composable("restaurant") {}
            }

            Column(Modifier.scrollable(rememberScrollState(), orientation = Orientation.Horizontal)) {
                Button(onClick = { selectedMarkerData = list[Random.Default.nextInt(list.size - 1)] }) {
                    Text(text = "pageChange") }
                Button(onClick = { coroutineScope.launch { cameraPositionState.animate(CameraUpdateFactory.zoomIn()) } }) {
                    Text(text = "+") }
                Button(onClick = { coroutineScope.launch { cameraPositionState.animate(CameraUpdateFactory.zoomOut()) } }) {
                    Text(text = "-") }
                CurrentLocationScreen(onLocation = {
                    location = it
                    isMyLocationEnabled = !isMyLocationEnabled; coroutineScope.launch {
                    cameraPositionState.animate(update = CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude)), 300)
                    myLocation = LatLng(it.latitude, it.longitude) } })
                Button(onClick = { navHostController.navigate("restaurant") }) { Text(text = "aa") }
                Button(onClick = { boundary = 100.0 }) { Text(text = "100M") }
                Button(onClick = { boundary = 200.0 }) { Text(text = "200M") }
                Button(onClick = { boundary = 500.0 }) { Text(text = "500M") }
                Button(onClick = { boundary = 1000.0 }) { Text(text = "1000M") }
                Button(onClick = { boundary = 3000.0 }) { Text(text = "3000M") }
                Button(onClick = { coroutineScope.launch { findRepository.search(Filter()) }}) { Text(text = "filter") }
            }
        }
    }
}