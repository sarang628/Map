package com.sarang.torang

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sarang.torang.data.RestaurantWithFiveImages
import com.sarang.torang.di.repository.FindRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Composable
fun RestaurantList(
    findRepository: FindRepositoryImpl,
    state               : LazyListState
){
    val tag = "__RestaurantList"
    val coroutineScope      : CoroutineScope                    = rememberCoroutineScope()
    var restaurants         : List<RestaurantWithFiveImages>    by remember { mutableStateOf(listOf()) }
    LaunchedEffect(Unit) {
        findRepository.restaurants.stateIn(scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = listOf()).collect {
            restaurants = it
        }
    }
    Log.w(tag, "recomposition")

    LazyColumn(modifier = Modifier
        .width(150.dp)
        .height(200.dp), state = state)
    {
        items(restaurants.size)
        {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        findRepository.selectRestaurant(restaurants[it].restaurant.restaurantId)
                    }
                }
            )
            {
                Text(restaurants[it].restaurant.restaurantName.toString())
            }
        }
    }
}