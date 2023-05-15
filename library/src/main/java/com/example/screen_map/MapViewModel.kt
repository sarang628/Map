package com.example.screen_map

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.JsonToObjectGenerator
import com.example.library.data.Restaurant
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.streams.toList

data class MapUiState(
    val list: List<MarkerData>? = null,
    val currentPosition: Int = 0,
    val move: MarkerData? = null
) {
    fun currentLatLng(): LatLng {
        return list?.get(currentPosition)?.getLatLng() ?: LatLng(0.0, 0.0)
    }
}

class MapViewModel(context: Context) : ViewModel() {
    val list = JsonToObjectGenerator<Restaurant>().getListByFile(
        context,
        "restaurants.json",
        Restaurant::class.java
    )

    val mapUiStateFlow = MutableStateFlow(MapUiState())

    init {
        viewModelScope.launch {
            mapUiStateFlow.emit(
                mapUiStateFlow.value.copy(
                    list = list.stream().map { it.toMarkerData() }.toList()
                )
            )
        }
    }

    fun selectRestaurant(restaurant: Restaurant) {
        val r = list.find {
            it.restaurant_id == restaurant.restaurant_id
        }
        viewModelScope.launch {
            mapUiStateFlow.emit(
                mapUiStateFlow.value.copy(
                    move = r?.toMarkerData()
                )
            )
        }
    }

}

fun Restaurant.toMarkerData(): MarkerData {
    return MarkerData(
        lat = lat ?: 0.0,
        lon = lon ?: 0.0,
        title = restaurant_name ?: ""
    )
}