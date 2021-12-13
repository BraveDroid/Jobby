package com.bravedroid.jobby.auth.di

import com.bravedroid.jobby.auth.AuthServiceConstants.BASE_URL
import com.bravedroid.jobby.auth.AuthenticationInterceptor
import com.bravedroid.jobby.auth.datasource.AuthDataSource
import com.bravedroid.jobby.auth.datasource.TokenProvider
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenRequestDto
import com.bravedroid.jobby.auth.service.AuthService
import com.bravedroid.jobby.auth.service.UserService
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.logger.NetworkLogger
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [NetworkBuilderHiltModule::class])
@InstallIn(SingletonComponent::class)
abstract class NetworkHiltModule

@Module
@InstallIn(SingletonComponent::class)
class NetworkBuilderHiltModule {
    @Singleton
    @Provides
    fun providesOkHttpClient(
        networkLogger: NetworkLogger,
        tokenProvider: TokenProvider,
        authDataSource: Lazy<AuthDataSource>,
        logger: Logger,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .callTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(AuthenticationInterceptor(tokenProvider))
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()
                var response = chain.proceed(request)
                logger.log(tag = "OkHttp", msg = " Before Start RunBlocking ")
                logger.log(tag = "OkHttp", msg = " current thread ${Thread.currentThread().name} ")
                runBlocking {
                    logger.log(tag = "OkHttp", msg = " current thread runBlocking ${Thread.currentThread().name} ")
                    if (response.code == 401) {
                        response.body?.close()
                        logger.log(tag = "OkHttp", msg = "response.code == 401")
                        logger.log(tag = "OkHttp", msg = " current thread ${Thread.currentThread().name} ")
                        authDataSource.get().refreshToken(
                            RefreshTokenRequestDto(
                                tokenProvider.refreshToken
                            )
                        ).catch { e -> logger.log(tag = "OkHttp", t = e) }
                            .collect {
                            logger.log(tag = "OkHttp", msg = " current thread collect${Thread.currentThread().name} ")
                            if (it is DomainResult.Success) {
                                logger.log(
                                    tag = "OkHttp",
                                    msg = " Success RefreshTokenResponse "
                                )
                                tokenProvider.accessToken = it.data.accessToken
                                val newRequest = chain.request().newBuilder()
                                    .removeHeader(
                                        "Authorization",
                                    ).addHeader(
                                        "Authorization",
                                        "Bearer ${tokenProvider.accessToken}"
                                    ).build()
//                                response = withContext(Dispatchers.IO) {
                                response = chain.proceed(newRequest)
//                                }
                            } else if (it is DomainResult.Error) {
                                logger.log(
                                    tag = "OkHttp",
                                    msg = "RefreshTokenResponse contains Error",
                                    priority = Priority.E
                                )
                            }
                        }
//                        }
//                        logger.log(tag = "OkHttp", msg = " End withContext ")
                    }
                    logger.log(tag = "OkHttp", msg = " End RunBlocking ")
                }
                logger.log(tag = "OkHttp", msg = " after End RunBlocking ")
                response
            })
            .addInterceptor(networkLogger.applicationLoggingInterceptor)
            .addNetworkInterceptor(networkLogger.networkLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    @NetworkQualifiers
    fun providesJobbyServerRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }.asConverterFactory("application/json".toMediaType()))
        .build()


    @Singleton
    @Provides
    fun providesAuthService(@NetworkQualifiers retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun providesUserService(@NetworkQualifiers retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)
}
