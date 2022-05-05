package com.example.screen_map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.torang_core.repository.FindRepository
import com.example.torang_core.repository.MapRepository
import com.example.torang_core.repository.NationRepository
import com.example.torang_core.util.Logger
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapRepository,
    private val nationRepository: NationRepository,
    private val findRepository: FindRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState

    private val _cameraUpdate = MutableLiveData<CameraUpdate>()
    val cameraUpdate: LiveData<CameraUpdate> = _cameraUpdate

    val selectdNationItem get() = nationRepository.getSelectNationItem()

    val clickMap = mapRepository.getClickMap()

    init {
        viewModelScope.launch {
            //내 위치 요청 시 처리
            findRepository.isRequestingLocation().collect { b ->
                _uiState.update {
                    it.copy(requestMyLocation = b)
                }
            }
        }
    }

    fun loadRestaurant() {
        viewModelScope.launch {
            try {
                mapRepository.loadRestaurant()
            } catch (e: Exception) {
                Logger.e(e.toString())
            }
        }
    }

    fun setLocation(latitude: Double, longitude: Double, zoom: Float = 16f) {
        _cameraUpdate.postValue(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(latitude, longitude),
                zoom
            )
        )
    }

    fun clickMap() {
        viewModelScope.launch {
            mapRepository.clickMap()
        }
    }

    fun setNorthEastLatitude(latitude: Double) {
        mapRepository.setNorthEastLatitude(latitude)
    }

    fun setNorthEastLongitude(longitude: Double) {
        mapRepository.setNorthEastLongitude(longitude)
    }

    fun setSouthWestLatitude(latitude: Double) {
        mapRepository.setSouthWestLatitude(latitude)
    }

    fun setSouthWestLongitude(longitude: Double) {
        mapRepository.setSouthWestLongitude(longitude)
    }

    fun onReceiveLocation() {
        viewModelScope.launch {
            delay(1000)
            findRepository.notifyReceiveLocation()
        }
    }
}