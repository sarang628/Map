package com.example.testapp.di.map

import com.example.screen_map.MapService
import com.example.screen_map.MarkerData
import com.sryang.torang_repository.api.ApiRestaurant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.streams.toList

@InstallIn(SingletonComponent::class)
@Module
class MapServiceModule {


    @Provides
    fun provideMapService(
        restaurantApi: ApiRestaurant
    ): MapService {
        return object : MapService {
            override suspend fun restaurantMarkerList(): List<MarkerData> {

                val list = restaurantApi.getAllRestaurant(HashMap())

                return list.stream().map {
                    MarkerData(
                        id = it.restaurantId,
                        lat = it.lat,
                        lon = it.lon,
                        title = it.restaurantName,
                        snippet = "",
                        foodType = it.restaurantType
                    )
                }.toList()
            }
        }
    }
}