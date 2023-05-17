package com.example.screen_map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    uiStateFlow: StateFlow<MapUiState>,
    onMark: ((Int) -> Unit)? = null
) {

    val uiState by uiStateFlow.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        uiState.list?.let {
            position = CameraPosition.fromLatLngZoom(
                it[0].getLatLng(), 15f
            )
        }
    }
    val scope = rememberCoroutineScope()

    uiState.move?.let {
        scope.launch {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLng(uiState.move!!.getLatLng())
            )
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        uiState.list?.let {
            for (data: MarkerData in it) {
                Marker(
                    state = data.markState(),
                    title = data.title,
                    snippet = data.snippet,
                    onClick = {
                        onMark?.invoke(Integer.parseInt(it.tag.toString()))
                        Log.d("sryang123", it.tag.toString())
                        false
                    },
                    tag = data.id
                )
            }
        }
    }
}


@Preview
@Composable
private fun TestMapScreen() {
    val viewModel = MapViewModel(LocalContext.current)
    MapScreen(uiStateFlow = viewModel.mapUiStateFlow)
}