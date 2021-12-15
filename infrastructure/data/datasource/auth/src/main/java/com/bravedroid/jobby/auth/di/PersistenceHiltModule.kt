package com.bravedroid.jobby.auth.di

import android.content.SharedPreferences
import com.bravedroid.jobby.auth.factories.RefreshTokenSharedPreferencesFactory
import com.bravedroid.jobby.auth.factories.RefreshTokenSharedPreferencesFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [PersistenceBuilderHiltModule::class])
@InstallIn(SingletonComponent::class)
abstract class PersistenceHiltModule {

    @Singleton
    @Binds
    abstract fun bindsRefreshTokenSharedPreferencesFactory(
        sharedPreferencesFactoryImpl: RefreshTokenSharedPreferencesFactoryImpl,
    ): RefreshTokenSharedPreferencesFactory
}

@Module
@InstallIn(SingletonComponent::class)
class PersistenceBuilderHiltModule {
    @Singleton
    @RefreshTokenSharedPreferences
    @Provides
    fun providesRefreshTokenSharedPreferences(sharedPreferencesFactory: RefreshTokenSharedPreferencesFactory): SharedPreferences =
        sharedPreferencesFactory.create()

    @Singleton
    @RefreshTokenSharedPreferencesEditor
    @Provides
    fun providesRefreshTokenSharedPreferencesEditor(@RefreshTokenSharedPreferences sharedPreferences: SharedPreferences): SharedPreferences.Editor =
        sharedPreferences.edit()
}
