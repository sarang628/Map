package com.example.screen_map

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState

data class MarkerData(
    val id: Int,
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val title: String = "",
    val snippet: String = "",
    val foodType: String
) {
    @Composable
    fun markState(): MarkerState {
        val markState = MarkerState(position = getLatLng())
        return markState
    }

    fun getLatLng(): LatLng {
        return LatLng(lat, lon)
    }
}

val MarkerData.icon
    get() =
        if (this.foodType.lowercase().equals("korean")) R.drawable.ic_korean
        else if (this.foodType.lowercase().equals("japanese")) R.drawable.ic_japan
        else if (this.foodType.lowercase().equals("american")) R.drawable.ic_american
        else if (this.foodType.lowercase().equals("italian")) R.drawable.ic_italian
        else if (this.foodType.lowercase().equals("spanish")) R.drawable.ic_spanish
        else R.drawable.ic_food