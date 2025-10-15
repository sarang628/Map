package com.sarang.torang

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.screen_map.compose.MapScreen
import com.example.screen_map.compose.MapScreenForFinding
import com.example.screen_map.compose.MapScreenSingleRestaurantMarker
import com.example.screen_map.compose.MapState
import com.example.screen_map.compose.rememberMapState
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.testMarkArrayList
import com.example.screen_map.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.sarang.torang.data.RestaurantWithFiveImages
import com.sarang.torang.data.remote.response.FilterApiModel
import com.sarang.torang.di.map_di.MapScreenForFindingWithPermission
import com.sarang.torang.di.repository.FindRepositoryImpl
import com.sryang.torang.ui.TorangTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var findRepository : FindRepositoryImpl

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

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun MapTest(){
        val tag = "__MapTest"
        val coroutineScope      : CoroutineScope                    = rememberCoroutineScope()
        val list                : List<MarkerData>                  = testMarkArrayList()
        var location            : Location?                         by remember { mutableStateOf(null) }
        var isMyLocationEnabled : Boolean                           by remember { mutableStateOf(false) }
        var myLocation          : LatLng?                           by remember { mutableStateOf(null) }
        val navHostController   : NavHostController                 = rememberNavController()
        var boundary            : Double                            by remember { mutableStateOf(0.0) }
        val mapViewModel        : MapViewModel                      = hiltViewModel()
        val mapState            : MapState                          = rememberMapState()
        val state               : LazyListState                     = rememberLazyListState()

        Log.w(tag, "recomposition")

        ScrollLazyList(findRepository, state)
        MoveCamera(findRepository, mapState)

        Scaffold {
            Box(Modifier.padding(it)){
            NavHost(navController = navHostController, startDestination = "nav") {
                composable("nav") {
                    Column {
                        Spacer(modifier = Modifier.height(400.dp))
                        Button({navHostController.navigate("SimpleMapScreen")}) {
                            Text("SimpleMapScreen")
                        }
                        Button({navHostController.navigate("MapScreen")}) {
                            Text("MapScreen")
                        }
                        Button({navHostController.navigate("MapScreenForFindingWithPermission")}) {
                            Text("MapScreenForFindingWithPermission")
                        }
                        Button({navHostController.navigate("MapScreen")}) {
                            Text("MapScreen")
                        }
                        Button({navHostController.navigate("MapScreenForRestaurant")}) {
                            Text("MapScreenForRestaurant")
                        }
                    }
                }
                composable("SimpleMapScreen") {
                    MapScreen(
                        mapState = mapState,
                        showLog = true,
                        onMapLoaded = { mapState.setOnMapLoaded() }
                    )
                }
                composable("MapScreen") {
                    MapScreen(mapViewModel = mapViewModel, mapState = mapState)
                }
                composable("MapScreenForFindingWithPermission") {
                    MapScreenForFindingWithPermission {
                        MapScreenForFinding(mapViewModel = mapViewModel, mapState = mapState)
                    }
                }
                composable("MapScreenForRestaurant"){
                    MapScreenSingleRestaurantMarker(restaurantId = 1)
                }
                composable("restaurant") {

                }
            }

            Column {
                FlowRow(Modifier.scrollable(rememberScrollState(), orientation = Orientation.Horizontal)) {
                    AssistChip(onClick = { coroutineScope.launch { mapState.cameraPositionState.animate(CameraUpdateFactory.zoomIn()) } }, label = { Text(text = "+") })
                    AssistChip(onClick = { coroutineScope.launch { mapState.cameraPositionState.animate(CameraUpdateFactory.zoomOut()) } }, label = {Text(text = "-") })
                    AssistChip(onClick = { navHostController.navigate("restaurant") }, label = { Text(text = "move") })
                    Spacer(Modifier.width(3.dp))
                    AssistChip(onClick = { boundary = 100.0 }, label = { Text(text = "100M") })
                    Spacer(Modifier.width(3.dp))
                    AssistChip(onClick = { boundary = 200.0 }, label = { Text(text = "200M") })
                    Spacer(Modifier.width(3.dp))
                    AssistChip(onClick = { boundary = 500.0 }, label = { Text(text = "500M") })
                    Spacer(Modifier.width(3.dp))
                    AssistChip(onClick = { boundary = 1000.0 }, label = { Text(text = "1000M") })
                    Spacer(Modifier.width(3.dp))
                    AssistChip(onClick = { boundary = 3000.0 }, label = { Text(text = "3000M") })
                    Spacer(Modifier.width(3.dp))
                    AssistChip(onClick = { coroutineScope.launch { findRepository.search(FilterApiModel()) }}, label = {Text(text = "filter")})
                    Spacer(Modifier.width(3.dp))
                    AssistChip(onClick = { coroutineScope.launch { findRepository.findThisArea() }}, label = {Text(text = "bound")})
                    Spacer(Modifier.width(3.dp))
                }
                RestaurantList(findRepository, state)
            }
        }
    }}
}

@Composable
private fun MoveCamera(
    repository: FindRepositoryImpl,
    state: MapState
) {
    val tag = "__MoveCamera"
    LaunchedEffect(state.onMapLoaded) { //Unit 으로 하면 state.onMapLoaded를 false에서 true 바뀌었을 때 인식 못함.
        repository.selectedRestaurant.collect {
            if(state.onMapLoaded) { //TODO:: state 안으로 넣기
                state.cameraPositionState.animate(CameraUpdateFactory.newLatLng(LatLng(it.restaurant.lat, it.restaurant.lon)))
            }
            else{
                Log.d(tag, "카메라 이동 불가. state.onMapLoaded : false")
            }
        }
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ScrollLazyList(findRepository : FindRepositoryImpl, state : LazyListState){
    val tag = "__ScrollLazyList"
    Log.w(tag, "recomposition")

    LaunchedEffect(Unit) {
        findRepository.selectedRestaurant
            .flatMapLatest { selected ->
                findRepository.restaurants.map { list ->
                    list.indexOfFirst {
                        it.restaurant.restaurantId == selected.restaurant.restaurantId
                    }
                }
            }
            .collect { index ->
                if (index >= 0) {
                    state.animateScrollToItem(index)
                }
            }
    }
}

@Composable
fun RestaurantList(
    findRepository: FindRepositoryImpl,
    state               : LazyListState
){
    val tag = "__RestaurantList"
    val coroutineScope      : CoroutineScope                    = rememberCoroutineScope()
    val restaurants         : List<RestaurantWithFiveImages>    = findRepository.restaurants.collectAsState().value

    Log.w(tag, "recomposition")

    LazyColumn(modifier = Modifier
        .width(150.dp)
        .height(200.dp), state = state)
    {
        items(restaurants.size)
        {
            TextButton(
                onClick =
                    {
                        coroutineScope.launch {
                                findRepository.selectRestaurant(restaurants[it].restaurant.restaurantId)
                            }
                    }
                 )
            {
                Text(restaurants[it].restaurant.restaurantName.toString())
            }
        }
    }
}