package com.bravedroid.jobby.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module(includes = [OkHttpClientBuilderHiltModule::class])
@InstallIn(SingletonComponent::class)
abstract class OkHttpClientHiltModule

@Module
@InstallIn(SingletonComponent::class)
class OkHttpClientBuilderHiltModule {
    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            // TODO: 09/11/2021 RF: support cache and network interceptor for debugging tools, headers...
            .build()
}
