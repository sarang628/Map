package com.sarang.torang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screen_map.compose.MapScreen
import com.example.screen_map.compose.MapScreenForFinding
import com.example.screen_map.compose.MapScreenSingleRestaurantMarker
import com.example.screen_map.compose.MapState
import com.example.screen_map.compose.rememberMapState
import com.example.screen_map.viewmodels.MapViewModel
import com.sarang.torang.di.map_di.MapScreenForFindingWithPermission
import com.sarang.torang.di.repository.FindRepositoryImpl
import com.sryang.torang.ui.TorangTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var findRepository : FindRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val mapState        : MapState      = rememberMapState()
            val mapViewModel    : MapViewModel  = hiltViewModel()

            TorangTheme {
                Surface(Modifier.Companion.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    MoveCamera(findRepository, mapState)

                    Navigation(findRepository = findRepository,
                        mapState = mapState,
                        mapScreen = { MapScreen(mapViewModel = mapViewModel, mapState = mapState) },
                        mapScreenForFindingWithPermission = {
                           MapScreenForFindingWithPermission {
                           MapScreenForFinding(mapViewModel = mapViewModel, mapState = mapState)
                        }},
                        mapScreenForRestaurant = {
                            TestContainer(findRepository){ restairantId, restaurantName ->
                                MapScreenSingleRestaurantMarker(restaurantId = restairantId)
                            }
                        },
                    )
                }
            }
        }
    }
}