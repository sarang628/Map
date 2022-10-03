package com.example.testapp.di

import com.sryang.torang_repository.data.AppDatabase
import com.sryang.torang_repository.data.dao.RestaurantDao
import com.sryang.torang_repository.data.dao.SearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DaoModules {
    /** 로컬 데이터베이스의 사용자 관리 DAO 제공 */

    @Provides
    fun provideSearchDao(appDatabase: AppDatabase): SearchDao {
        return appDatabase.searchDao()
    }
    /** 로컬 데이터베이스의 사용자 관리 DAO 제공 */
    @Provides
    fun provideRestaurantDao(appDatabase: AppDatabase): RestaurantDao {
        return appDatabase.restaurantDao()
    }
}
