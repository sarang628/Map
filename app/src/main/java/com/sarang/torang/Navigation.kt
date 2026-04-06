package com.sarang.torang

import android.location.Location
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.screen_map.compose.MapState
import com.example.screen_map.compose.rememberMapState
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.testMarkArrayList
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.sarang.torang.api.ApiFilter
import com.sarang.torang.data.Filter
import com.sarang.torang.data.remote.response.CityApiModel
import com.sarang.torang.di.repository.FindRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun Navigation(findRepository: FindRepositoryImpl,
                        apiFilter     : ApiFilter,
                        mapScreenForRestaurant : @Composable () -> Unit = {},
                        mapScreenForFindingWithPermission : @Composable () -> Unit = {},
                        mapScreen : @Composable () -> Unit = {},
                        mapState : MapState,
                        navHostController : NavHostController = rememberNavController(),
                        onFindMyLocation : ()->Unit = {},
                        onBoundary         : (Double)->Unit        = {}){
    var cities              : List<CityApiModel> by remember { mutableStateOf(emptyList()) }
    val state               : LazyListState      = rememberLazyListState()
    val coroutineScope      : CoroutineScope     = rememberCoroutineScope()

    ScrollLazyList(findRepository, state)
    MoveCamera(findRepository, mapState)

    LaunchedEffect(Unit) {
        cities = apiFilter.getCities()
    }

    NavHost(navController = navHostController, startDestination = "menu") {
        composable("menu") { Menu(navHostController) }
        composable("MapScreen") {
            Scaffold() {
                Box(Modifier.padding(it)){
                    mapScreen()
                    TopActionButtons(mapState = mapState,
                                     cities = cities,
                                     onRestaurant = {navHostController.navigate("restaurant")},
                                     onSearch = {coroutineScope.launch { findRepository.search(Filter()) }},
                                     onFindThisArea = { coroutineScope.launch { findRepository.findThisArea() } },
                                     onSelectCity = { findRepository.setCameraPosition(Triple(it.latitude, it.longitude, it.zoom)) },
                                     onFindMyLocation = onFindMyLocation,
                                     onBoundary = onBoundary)
                }
            }
        }
        composable("MapScreenForFindingWithPermission") {
            mapScreenForFindingWithPermission()
            TopActionButtons(mapState = mapState,
                             cities = cities,
                             onRestaurant = {navHostController.navigate("restaurant")},
                             onSearch = {coroutineScope.launch { findRepository.search(Filter()) }},
                             onSelectCity = { findRepository.setCameraPosition(Triple(it.latitude, it.longitude, it.zoom)) },
                             onFindThisArea = { coroutineScope.launch { findRepository.findThisArea() } },
                             onFindMyLocation = onFindMyLocation,
                             onBoundary = onBoundary)
        }
        composable("MapScreenForRestaurant"){ mapScreenForRestaurant() }
    }

}


@Composable
fun Menu(navHostController : NavHostController){
    Scaffold {
        Column(Modifier.padding(it)) {
            Button({navHostController.navigate("MapScreen")}) {
                Text("MapScreen")
            }
            Button({navHostController.navigate("MapScreenForFindingWithPermission")}) {
                Text("MapScreenForFindingWithPermission")
            }
            Button({navHostController.navigate("MapScreenForRestaurant")}) {
                Text("MapScreenForRestaurant")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopActionButtons(mapState: MapState = rememberMapState(),
                     cities             : List<CityApiModel>    = emptyList(),
                     onRestaurant       : ()->Unit              = {},
                     onSearch           : ()->Unit              = {},
                     onFindThisArea     : ()->Unit              = {},
                     onSelectCity       : (CityApiModel)->Unit  = {},
                     onFindMyLocation   : ()->Unit              = {},
                     onBoundary         : (Double)->Unit        = {}
                     ){
    val coroutineScope      : CoroutineScope                    = rememberCoroutineScope()
    var boundary            : Double                            by remember { mutableStateOf(0.0) }
    var selectedCity        : String                            by remember { mutableStateOf("city") }
    var expanded by remember { mutableStateOf(false) }
    var expandedBoundary by remember { mutableStateOf(false) }
    Column {
        FlowRow(Modifier.scrollable(rememberScrollState(), orientation = Orientation.Horizontal)) {
            AssistChip(onClick = { coroutineScope.launch { mapState.cameraPositionState.animate(CameraUpdateFactory.zoomIn()) } }, label = { Text(text = "+") })
            AssistChip(onClick = { coroutineScope.launch { mapState.cameraPositionState.animate(CameraUpdateFactory.zoomOut()) } }, label = {Text(text = "-") })
            AssistChip(onClick = onRestaurant, label = { Text(text = "move") })
            Spacer(Modifier.width(3.dp))
            Box {
                AssistChip(onClick = { expandedBoundary = true }, label = { Text(text = "boundary : $boundary") })
                DropdownMenu(expanded = expandedBoundary,
                             onDismissRequest = {expandedBoundary = false}) {
                    DropdownMenuItem(onClick = { boundary = 100.0; onBoundary.invoke(100.0); expandedBoundary = false }, text = { Text(text = "100M") })
                    DropdownMenuItem(onClick = { boundary = 200.0; onBoundary.invoke(200.0); expandedBoundary = false }, text = { Text(text = "200M") })
                    DropdownMenuItem(onClick = { boundary = 500.0; onBoundary.invoke(500.0); expandedBoundary = false }, text = { Text(text = "500M") })
                    DropdownMenuItem(onClick = { boundary = 1000.0; onBoundary.invoke(1000.0); expandedBoundary = false }, text = { Text(text = "1000M") })
                    DropdownMenuItem(onClick = { boundary = 3000.0; onBoundary.invoke(3000.0); expandedBoundary = false }, text = { Text(text = "3000M") })
                }
            }
            Spacer(Modifier.width(3.dp))
            AssistChip(onClick = { onSearch() }, label = {Text(text = "filter")})
            Spacer(Modifier.width(3.dp))
            AssistChip(onClick = { onFindThisArea() }, label = {Text(text = "bound")})
            Spacer(Modifier.width(3.dp))
            Box {
                AssistChip(onClick = { expanded = true }, label = {Text(text = selectedCity)})
                DropdownMenu(expanded = expanded,
                             onDismissRequest = {expanded = false}) {
                    cities.forEach {
                        DropdownMenuItem(
                            text = { Text(it.name) },
                            onClick = {
                                selectedCity = it.name
                                onSelectCity.invoke(it)
                                expanded = false
                            })
                    }
                }
            }
            Spacer(Modifier.width(3.dp))
            AssistChip(onClick = onFindMyLocation, label = { Text("myLocation") })
        }
        //RestaurantList(findRepository, state)
    }
}

@Preview
@Composable
fun PreviewTopActionBar(){
    TopActionButtons {/*Preview*/

    }
}