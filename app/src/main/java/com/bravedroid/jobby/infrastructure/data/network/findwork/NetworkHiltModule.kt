package com.bravedroid.jobby.infrastructure.data.network.findwork

import com.bravedroid.jobby.infrastructure.data.network.findwork.service.FindWorkService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkBuilderHiltModule::class])
@InstallIn(SingletonComponent::class)
class NetworkHiltModule

@Module
@InstallIn(SingletonComponent::class)
class NetworkBuilderHiltModule {
    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            // TODO: 09/11/2021 RF: support cache and network interceptor for debugging tools, headers...
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(FindWorkConstants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }.asConverterFactory("application/json".toMediaType()))
        .build()


    @Singleton
    @Provides
    fun providesFindWorkService(retrofit: Retrofit): FindWorkService =
        retrofit.create(FindWorkService::class.java)
}
