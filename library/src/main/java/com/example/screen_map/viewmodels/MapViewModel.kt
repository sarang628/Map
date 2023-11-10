package com.example.screen_map.viewmodels

import androidx.lifecycle.ViewModel
import com.example.screen_map.uistate.MapUiState
import com.example.screen_map.usecase.SavePositionUseCase
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    val saveMapPositionUseCase: SavePositionUseCase
) : ViewModel() {
    fun saveCameraPosition(position: CameraPosition) {
        saveMapPositionUseCase.save(position = position)
    }

    fun getLastPosition(): LatLng {
        return saveMapPositionUseCase.load().target
    }

    fun getLastZoom(): Float {
        return saveMapPositionUseCase.load().zoom
    }


    val mapUiStateFlow = MutableStateFlow(
        MapUiState(currentPosition = 0)
    )

}