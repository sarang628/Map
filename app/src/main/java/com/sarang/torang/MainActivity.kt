package com.sarang.torang

import android.Manifest
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.sarang.torang.di.map_di.MapScreenForFindingWithPermission
import com.sarang.torang.di.repository.FindRepositoryImpl
import com.sryang.library.compose.workflow.BestPracticeViewModel
import com.sryang.torang.ui.TorangTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var findRepository : FindRepositoryImpl

    @OptIn(ExperimentalPermissionsApi::class)
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
                            val viewModel : BestPracticeViewModel = hiltViewModel()
                            val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
                            TestContainer(findRepository){ restairantId, restaurantName ->
                                MapScreenForFindingWithPermission(viewModel = viewModel) {
                                    MapScreenSingleRestaurantMarker(restaurantId = restairantId,
                                                                    requestPermission = { viewModel.request() },
                                                                    hasPermission = permissionState.status.isGranted)
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}