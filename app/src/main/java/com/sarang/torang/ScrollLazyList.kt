package com.sarang.torang

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.sarang.torang.di.repository.FindRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ScrollLazyList(findRepository : FindRepositoryImpl, state : LazyListState){
    val tag = "__ScrollLazyList"
    Log.w(tag, "recomposition")

    LaunchedEffect(Unit) {
        findRepository.selectedRestaurant
            .flatMapLatest { selected ->
                findRepository.restaurants.map { list ->
                    list.indexOfFirst {
                        it.restaurant.restaurantId == selected.restaurant.restaurantId
                    }
                }
            }
            .collect { index ->
                if (index >= 0) {
                    state.animateScrollToItem(index)
                }
            }
    }
}