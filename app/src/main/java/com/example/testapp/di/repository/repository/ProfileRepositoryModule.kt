package com.sryang.torang_repository.di.repository.repository

import com.sryang.torang_repository.repository.profile.ProfileRepository
import com.sryang.torang_repository.repository.profile.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileRepositoryModule {
    @Binds
    abstract fun provideLoginRepository(profileRepository: ProfileRepositoryImpl): ProfileRepository
}