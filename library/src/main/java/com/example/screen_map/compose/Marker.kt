package com.example.screen_map.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.screen_map.data.MarkerData
import com.example.screen_map.data.icon
import com.google.maps.android.compose.Marker

@Composable
fun Marker(markData       : MarkerData,
           onMark         : (Int) -> Unit   = {},
           visibleInfo    : Boolean         = false){
    val context = LocalContext.current
    Marker(
        state   = markData.markState(),
        snippet = markData.snippet,
        onClick = { onMark.invoke(markData.id); false },
        tag     = markData.id,
        icon    = markData.icon(context    = context,
                                title      = markData.title,
                                rating     = markData.rating,
                                isSelected = true,
                                price      = markData.price,
                                visibleTitle = visibleInfo,
                                visiblePriceAndRating = visibleInfo)
    )
}

@Composable
fun Markers(list            : List<MarkerData>,
            onMark          : (Int) -> Unit = {},
            visibleInfo     : Boolean = false){
    list.forEach {
        Marker(it, onMark, visibleInfo)
    }
}