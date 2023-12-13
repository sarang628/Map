package com.example.screen_map.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.screen_map.uistate.MapUiState
import com.example.screen_map.usecase.SavePositionUseCase
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val saveMapPositionUseCase: SavePositionUseCase
) : ViewModel() {
    var isMapLoaded = MutableStateFlow(false)

    fun saveCameraPosition(state: CameraPositionState) {
        saveMapPositionUseCase.save(position = state.position)
        state.projection?.visibleRegion?.let {
            saveMapPositionUseCase.saveBound(it)
        }
    }

    fun getLastPosition(): LatLng {
        return saveMapPositionUseCase.load().target
    }

    fun getLastZoom(): Float {
        return saveMapPositionUseCase.load().zoom
    }

    fun onMapLoaded() {
        isMapLoaded.update { true }
    }


    val mapUiStateFlow = MutableStateFlow(
        MapUiState(currentPosition = 0)
    )

}