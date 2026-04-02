package com.sarang.torang

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sarang.torang.repository.FindRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestContainer(findRepository: FindRepository,
                  onRestaurant : (Int)->Unit = {},
                  content : @Composable (Int, String)->Unit = {_,_->}){
    val restaurants by findRepository.restaurants.collectAsStateWithLifecycle(emptyList())
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    val sharedPref = LocalContext.current.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    var restaurantId by remember { mutableStateOf(sharedPref.getInt("restaurant_id", 0)) }
    var restaurantName by remember { mutableStateOf("") }

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(restaurantId) {
        sharedPref.edit().putInt("restaurant_id", restaurantId).apply()
    }

    LaunchedEffect(Unit) {
        // 마지막 선택한 음식점명 상단에 보여주려고 한번 불러옴
        findRepository.findFilter()
    }

    LaunchedEffect(restaurants) {
        //첫 진입 시 마지막 선택 음식점 보여주기위해
        restaurants.find { it.restaurant.restaurantId == restaurantId }?.let {
            restaurantName = it.restaurant.restaurantName
        }
    }


    Box{
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,
            sheetContent = {
                LazyColumn(Modifier.fillMaxSize()) {
                    item {
                        TextField(modifier = Modifier.fillMaxWidth(),
                                  value = searchText,
                                  onValueChange = {searchText = it})
                    }
                    items(restaurants
                        .filter { it.restaurant.restaurantName.lowercase().contains(searchText.lowercase()) }
                        .reversed()){
                        TextButton({
                            restaurantId = it.restaurant.restaurantId
                            restaurantName = it.restaurant.restaurantName
                            scope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                        }) {
                            Text(it.restaurant.restaurantName)
                        }
                    }
                }
            }
        ){
            onRestaurant(restaurantId)
            content(restaurantId, restaurantName)
        }
        AssistChip(modifier = Modifier.align(Alignment.BottomCenter),
            onClick = {
                onRestaurant(restaurantId)},
            label = { Text("refresh") })

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp, end = 12.dp),
            onClick = {
                scope.launch {
                    findRepository.findFilter()
                    scaffoldState.bottomSheetState.expand()
                }
            }) {
            Icon(Icons.AutoMirrored.Default.List, null)
        }
    }
}