package com.example.screen_map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(InternalCoroutinesApi::class)
@HiltViewModel
class MapViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState

    private val _cameraUpdate = MutableLiveData<CameraUpdate>()
    val cameraUpdate: LiveData<CameraUpdate> = _cameraUpdate

    //val clickMap = mapRepository.getClickMap()

    init {
        viewModelScope.launch {
            //내 위치 요청 시 처리
            /*findRepository.isRequestingLocation().collect(FlowCollector { b ->
                _uiState.update {
                    it.copy(requestMyLocation = b)
                }
            })*/
        }

        viewModelScope.launch {
            /*mapRepository.getCurrentLocationFlow().collect(FlowCollector {location ->
                _uiState.update {
                    it.copy(currentLocation = location)
                }
            })*/
        }

        viewModelScope.launch {
            /*nationRepository.getSelectNationItem().collect(FlowCollector {nationItem->
                _uiState.update {
                    it.copy(selectedNationItem = nationItem)
                }
            })*/
        }

        viewModelScope.launch {
            /*findRepository.getSearchedRestaurant().collect(FlowCollector { restaurants ->
                _uiState.update {
                    it.copy(
                        searchedRestaurants = restaurants
                    )
                }
            })*/
        }

        viewModelScope.launch {
            /*findRepository.getCurrentPosition().collect(FlowCollector { position ->
                _uiState.update {
                    it.copy(
                        position = position
                    )
                }
            })*/
        }
    }

    fun clickMap() {
        /*viewModelScope.launch {
            findRepository.clickMap()
        }*/
    }

    fun setNorthEastLatitude(latitude: Double) {
//        mapRepository.setNorthEastLatitude(latitude)
    }

    fun setNorthEastLongitude(longitude: Double) {
//        mapRepository.setNorthEastLongitude(longitude)
    }

    fun setSouthWestLatitude(latitude: Double) {
//        mapRepository.setSouthWestLatitude(latitude)
    }

    fun setSouthWestLongitude(longitude: Double) {
//        mapRepository.setSouthWestLongitude(longitude)
    }

    fun onReceiveLocation() {
        /*viewModelScope.launch {
            delay(1000)
            findRepository.notifyReceiveLocation()
        }*/
    }

    fun selectPosition(indexOf: Int) {
        /*viewModelScope.launch {
            findRepository.setCurrentPosition(indexOf)
        }*/
    }
}