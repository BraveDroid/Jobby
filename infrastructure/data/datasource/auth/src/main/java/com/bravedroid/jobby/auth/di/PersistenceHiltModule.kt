package com.bravedroid.jobby.auth.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.bravedroid.jobby.auth.datasource.RefreshTokenStore.Companion.PERSISTENCE_FILE_NAME
import com.bravedroid.jobby.auth.datasource.RefreshTokenStoreSecure.Companion.PERSISTENCE_FILE_NAME_SECURE
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
    @RefreshTokenSharedPreferencesSecure
    @Singleton
    fun providesRefreshTokenSharedPreferencesSecure(@ApplicationContext context: Context): SharedPreferences =
        EncryptedSharedPreferences.create(
            PERSISTENCE_FILE_NAME_SECURE,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )


    @Provides
    @RefreshTokenSharedPreferencesEditor
    @Singleton
    fun providesRefreshTokenSharedPreferencesEditor(@RefreshTokenSharedPreferences sharedPreferences: SharedPreferences): SharedPreferences.Editor =
        sharedPreferences.edit()

    @Provides
    @RefreshTokenSharedPreferencesEditorSecure
    @Singleton
    fun providesRefreshTokenSharedPreferencesEditorSecure(@RefreshTokenSharedPreferencesSecure sharedPreferences: SharedPreferences): SharedPreferences.Editor =
        sharedPreferences.edit()

}
