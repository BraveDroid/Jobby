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
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.getErrorOrNull
import com.bravedroid.jobby.logger.NetworkLogger
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

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
        @ApplicationScope applicationCoroutineScope: CoroutineScope,
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
                runBlocking(applicationCoroutineScope.coroutineContext) {
//                    logger.log(tag = "OkHttp", msg = " Start RunBlocking ")
//                    withContext(applicationCoroutineScope.coroutineContext) {
                    logger.log(tag = "OkHttp", msg = " Start withContext ")
                    if (response.code == 401) {
                        response.body?.close()
                        logger.log(tag = "OkHttp", msg = "response.code == 401")
                        authDataSource.get().refreshToken(
                            RefreshTokenRequestDto(
                                tokenProvider.refreshToken
                            )
                        ).catch { e ->
                            logger.log(tag = "OkHttp", t = e)
                        }.collect {
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
