package com.sarang.torang

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.screen_map.compose.MapState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.sarang.torang.di.repository.FindRepositoryImpl

@Composable
internal fun MoveCamera(
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