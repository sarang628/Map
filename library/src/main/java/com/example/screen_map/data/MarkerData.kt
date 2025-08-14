package com.example.screen_map.data

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.core.graphics.createBitmap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import com.sarang.torang.R

data class MarkerData(
    val id: Int = -1,
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val title: String = "",
    val snippet: String = "",
    val foodType: String = "",
    val rating : String = "",
    val price : String = ""
) {
    @Composable
    fun markState(): MarkerState = MarkerState(position = getLatLng())
    fun getLatLng(): LatLng = LatLng(lat, lon)
}

fun MarkerData.icon(context: Context, title: String, rating: String, isSelected : Boolean = false, price : String = "", visibleTitle : Boolean = true, visiblePriceAndRating : Boolean = true) =
        if (this.foodType.lowercase() == "kr") createCustomMarker(context = context, iconRes = R.drawable.ic_korea, isSelected = isSelected, title = title, rating = rating, price = price, visibleTitle = visibleTitle, visiblePriceAndRating = visiblePriceAndRating)
        else if (this.foodType.lowercase() == "jp")createCustomMarker(context = context, iconRes = R.drawable.ic_japan, isSelected = isSelected, title = title, rating = rating, price = price, visibleTitle = visibleTitle, visiblePriceAndRating = visiblePriceAndRating)
        else if (this.foodType.lowercase() == "am") createCustomMarker(context = context, iconRes = R.drawable.ic_us, isSelected = isSelected, title = title, rating = rating, price = price, visibleTitle = visibleTitle, visiblePriceAndRating = visiblePriceAndRating)
        else if (this.foodType.lowercase() == "it") createCustomMarker(context = context, iconRes = R.drawable.ic_italy, isSelected = isSelected, title = title, rating = rating, price = price, visibleTitle = visibleTitle, visiblePriceAndRating = visiblePriceAndRating)
        else if (this.foodType.lowercase() == "sp") createCustomMarker(context = context, iconRes = R.drawable.ic_spain, isSelected = isSelected, title = title, rating = rating, price = price, visibleTitle = visibleTitle, visiblePriceAndRating = visiblePriceAndRating)
        else if (this.foodType.lowercase() == "vn") createCustomMarker(context = context, iconRes = R.drawable.ic_vn, isSelected = isSelected, title = title, rating = rating, price = price, visibleTitle = visibleTitle, visiblePriceAndRating = visiblePriceAndRating)
        else if (this.foodType.lowercase() == "cn") createCustomMarker(context = context, iconRes = R.drawable.ic_cn, isSelected = isSelected, title = title, rating = rating, price = price, visibleTitle = visibleTitle, visiblePriceAndRating = visiblePriceAndRating)
        else if (this.foodType.lowercase() == "cf") createCustomMarker(context = context, iconRes = R.drawable.ic_coffee, isSelected = isSelected, title = title, rating = rating, price = price, visibleTitle = visibleTitle, visiblePriceAndRating = visiblePriceAndRating)
        else if (this.foodType.lowercase() == "ph") createCustomMarker(context = context, iconRes = R.drawable.ic_ph, isSelected = isSelected, title = title, rating = rating, price = price, visibleTitle = visibleTitle, visiblePriceAndRating = visiblePriceAndRating)
        else if (this.foodType.lowercase() == "ff") createCustomMarker(context = context, iconRes = R.drawable.ic_ff, isSelected = isSelected, title = title, rating = rating, price = price, visibleTitle = visibleTitle, visiblePriceAndRating = visiblePriceAndRating)
        else createCustomMarker(context = context, isSelected = isSelected, title = title, rating = rating, price = price, visibleTitle = visibleTitle, visiblePriceAndRating = visiblePriceAndRating)

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

fun createCustomMarker(context: Context, iconRes : Int? = null, isSelected: Boolean, title: String, rating: String, price : String = "", visibleTitle : Boolean = true, visiblePriceAndRating : Boolean = true): BitmapDescriptor {
    val markerView = LayoutInflater.from(context).inflate(if(isSelected)R.layout.view_selected_custom_marker else R.layout.view_custom_marker, null)

    val icon = markerView.findViewById<ImageView>(R.id.markerIcon)
    val name = markerView.findViewById<TextView>(R.id.markerTitle)
    val ratingText = markerView.findViewById<TextView>(R.id.markerRating)
    val markerPrice = markerView.findViewById<TextView>(R.id.markerPrice)

    name.visibility = if(visibleTitle) View.VISIBLE else View.GONE
    ratingText.visibility = if(visiblePriceAndRating) View.VISIBLE else View.GONE
    markerPrice.visibility = if(visiblePriceAndRating) View.VISIBLE else View.GONE

    iconRes?.let { icon.setImageResource(it) }
    name.text = title
    ratingText.text = rating
    markerPrice.text = price

    markerView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
    markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)

    val bitmap = createBitmap(markerView.measuredWidth, markerView.measuredHeight)
    val canvas = Canvas(bitmap)
    markerView.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}