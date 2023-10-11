package com.sryang.torang_repository.di.repository.repository

import com.sryang.torang_repository.repository.InfoRepository
import com.sryang.torang_repository.repository.MenuRepository
import com.sryang.torang_repository.repository.ReviewRepository
import com.sryang.torang_repository.repository.info.InfoRepositoryImpl
import com.sryang.torang_repository.repository.map.MapRepository
import com.sryang.torang_repository.repository.map.MapRepositoryImpl
import com.sryang.torang_repository.repository.menu.MenuRepositoryImpl
import com.sryang.torang_repository.repository.review.ReviewRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ReviewRepositoryModule {
    @Binds
    abstract fun provideInfoRepository(infoRepositoryImpl: InfoRepositoryImpl): InfoRepository

    @Binds
    abstract fun provideReviewRepository(reviewRepositoryImpl: ReviewRepositoryImpl): ReviewRepository

    @Binds
    abstract fun provideMenuRepository(menuRepositoryImpl: MenuRepositoryImpl): MenuRepository

    @Binds
    abstract fun provideMapRepository(mapRepositoryImpl: MapRepositoryImpl): MapRepository
}