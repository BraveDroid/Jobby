package com.bravedroid.jobby.auth.di

import android.content.SharedPreferences
import com.bravedroid.jobby.auth.datasource.RefreshTokenStore
import com.bravedroid.jobby.auth.datasource.RefreshTokenStoreImpl
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
abstract class PersistenceHiltModule{

    @Singleton
    @Binds
    abstract fun bindsRefreshTokenSharedPreferencesFactory(sharedPreferencesFactoryImpl: RefreshTokenSharedPreferencesFactoryImpl): RefreshTokenSharedPreferencesFactory

    @Singleton
    @Binds
    abstract fun bindRefreshTokenStore(refreshTokenStoreImpl: RefreshTokenStoreImpl):RefreshTokenStore
}

@Module
@InstallIn(SingletonComponent::class)
class PersistenceBuilderHiltModule {
    @Provides
    @Singleton
    fun providesRefreshTokenSharedPreferences(sharedPreferencesFactory : RefreshTokenSharedPreferencesFactory): SharedPreferences =
        sharedPreferencesFactory.create()

    @Provides
    @Singleton
    fun providesRefreshTokenSharedPreferencesEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor =
        sharedPreferences.edit()
}
