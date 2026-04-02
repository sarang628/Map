package com.example.screen_map.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.icon
import com.google.maps.android.compose.Marker

@Composable
fun Marker(it : MarkerData){
    val context = LocalContext.current
    Marker(
        state = it.markState(),
        snippet = it.snippet,
        onClick = { false },
        tag = it.id,
        icon = it.icon(
            context = context,
            title = it.title,
            rating = it.rating,
            isSelected = true,
            price = it.price
        )
    )
}