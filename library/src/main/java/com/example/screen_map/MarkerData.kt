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


fun testMarkArrayList(): List<MarkerData> {
    return ArrayList<MarkerData>().apply {
        add(MarkerData(1, 10.329937685815878, 123.90560215206462, "1", "", ""))
        add(MarkerData(2, 10.330901483406983, 123.9070398556124, "2", "", ""))
        add(MarkerData(3, 10.33050042424096, 123.90578456184056, "3", "", ""))
        add(MarkerData(4, 10.33079559690662, 123.9055002993406, "4", "", ""))
        add(MarkerData(5, 10.330742812582061, 123.90729203228928, "5", "", ""))
        add(MarkerData(6, 10.32581095372999, 123.90526528558216, "6", "", ""))
        add(MarkerData(7, 10.32876903844882, 123.90572559560252, "7", "", ""))
    }
}