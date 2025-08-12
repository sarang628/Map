package com.example.screen_map.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import com.sarang.torang.R
import androidx.core.graphics.createBitmap

data class MarkerData(
    val id: Int = -1,
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val title: String = "",
    val snippet: String = "",
    val foodType: String = "",
    val rating : String = ""
) {
    @Composable
    fun markState(): MarkerState = MarkerState(position = getLatLng())
    fun getLatLng(): LatLng = LatLng(lat, lon)
}

fun MarkerData.icon(context: Context, title: String, rating: String) =
        if (this.foodType.lowercase() == "kr") BitmapDescriptorFactory.fromResource(R.drawable.ic_korea)
        else if (this.foodType.lowercase() == "jp") BitmapDescriptorFactory.fromResource(R.drawable.ic_japan)
        else if (this.foodType.lowercase() == "am") BitmapDescriptorFactory.fromResource(R.drawable.ic_us)
        else if (this.foodType.lowercase() == "it") BitmapDescriptorFactory.fromResource(R.drawable.ic_italy)
        else if (this.foodType.lowercase() == "sp") BitmapDescriptorFactory.fromResource(R.drawable.ic_spain)
        else if (this.foodType.lowercase() == "vn") BitmapDescriptorFactory.fromResource(R.drawable.ic_vn)
        else if (this.foodType.lowercase() == "cf") BitmapDescriptorFactory.fromResource(R.drawable.ic_coffee)
        else createCustomMarker(context, R.drawable.ic_pointer, title, rating)
        //else R.drawable.ic_pointer


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

fun createCustomMarker(context: Context, iconResId: Int, title: String, rating: String): BitmapDescriptor {
    val markerView = LayoutInflater.from(context).inflate(R.layout.view_custom_marker, null)

    val icon = markerView.findViewById<ImageView>(R.id.markerIcon)
    val name = markerView.findViewById<TextView>(R.id.markerTitle)
    val ratingText = markerView.findViewById<TextView>(R.id.markerRating)

    icon.setImageResource(iconResId)
    name.text = title
    ratingText.text = rating

    markerView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
    markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)

    val bitmap = createBitmap(markerView.measuredWidth, markerView.measuredHeight)
    val canvas = Canvas(bitmap)
    markerView.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}