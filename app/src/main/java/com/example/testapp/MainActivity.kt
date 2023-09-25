package com.example.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.screen_map.MapScreen
import com.example.screen_map.MapUiState
import com.example.screen_map.MapViewModel
import com.example.screen_map.MarkerData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val mapViewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapScreen(
                mapViewModel = mapViewModel,
                uiStateFlow = MutableStateFlow<MapUiState>(
                    MapUiState(
                        list = ArrayList(),
                        currentPosition = 0,
                        move = MarkerData(
                            id = 0,
                            lat = 0.0,
                            lon = 0.0,
                            title = "",
                            snippet = ""
                        )
                    )
                )
            )
        }
    }
}