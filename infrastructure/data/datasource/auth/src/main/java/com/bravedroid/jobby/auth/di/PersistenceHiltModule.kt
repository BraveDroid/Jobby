package com.bravedroid.jobby.auth.di

import android.content.Context
import android.content.SharedPreferences
import com.bravedroid.jobby.auth.datasource.RefreshTokenStore.Companion.PERSISTENCE_FILE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [PersistenceBuilderHiltModule::class])
@InstallIn(SingletonComponent::class)
abstract class PersistenceHiltModule

@Module
@InstallIn(SingletonComponent::class)
class PersistenceBuilderHiltModule {
    @Provides
    @RefreshTokenSharedPreferences
    @Singleton
    fun providesRefreshTokenSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
    context.getSharedPreferences(PERSISTENCE_FILE_NAME, Context.MODE_PRIVATE)

    @Provides
    @RefreshTokenSharedPreferencesEditor
    @Singleton
    fun providesRefreshTokenSharedPreferencesEditor(@RefreshTokenSharedPreferences sharedPreferences: SharedPreferences): SharedPreferences.Editor =
        sharedPreferences.edit()
}
