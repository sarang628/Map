package com.example.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screen_map.MapScreen
import com.example.screen_map.MapService
import com.example.screen_map.MapUiState
import com.example.screen_map.MapViewModel
import com.example.screen_map.MarkerData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mapViewModel: MapViewModel by viewModels()

    @Inject
    lateinit var mapService: MapService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Box {
                MapScreen(
                    mapViewModel = mapViewModel,
                    animationMoveDuration = 300,
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
                Button(onClick = { mapViewModel.selectRestaurantById(Random.nextInt(10)) }) {
                    Text(text = "pageChange")
                }
            }
        }
    }
}