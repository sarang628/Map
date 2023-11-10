package com.example.testapp.di.map

import com.example.screen_map.data.MarkerData
import com.example.screen_map.usecase.MapService
import com.example.screen_map.usecase.SavePositionUseCase
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.sryang.torang_repository.api.ApiRestaurant
import com.sryang.torang_repository.repository.MapRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

    @Provides
    fun provideSavePositionUseCase(
        mapRepository: MapRepository
    ): SavePositionUseCase {
        return object : SavePositionUseCase {
            override fun save(position: CameraPosition) {
                mapRepository.saveLat(position.target.latitude)
                mapRepository.savelon(position.target.longitude)
                mapRepository.saveZoom(position.zoom)
            }

            override fun load(): CameraPosition {
                return CameraPosition(
                    LatLng(mapRepository.loadLat(), mapRepository.loadLon()),
                    mapRepository.loadZoom(),
                    0f,
                    0f
                )
            }
        }
    }
}