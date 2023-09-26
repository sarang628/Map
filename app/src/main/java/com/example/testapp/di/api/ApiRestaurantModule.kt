package com.sryang.torang_repository.di.api

import com.sryang.torang_repository.api.ApiRestaurant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiRestaurantModule {
    @Singleton
    @Provides
    fun provideRemoteFeedService(
        apiRestaurant: ProductApiRestaurant
    ): ApiRestaurant {
        return apiRestaurant.create()
    }
}

@Singleton
class ProductApiRestaurant @Inject constructor(
    private val torangOkHttpClientImpl: TorangOkhttpClient,
    private val retrofitModule: RetrofitModule
) {
    private var url = "http://sarang628.iptime.org:8081/"
    fun create(): ApiRestaurant {
        return retrofitModule
//            .getRetrofit(torangOkHttpClientImpl.getUnsafeOkHttpClient(), url)
            .getRetrofit(torangOkHttpClientImpl.getHttpClient(), url)
            .create(ApiRestaurant::class.java)
    }
}