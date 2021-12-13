package com.bravedroid.jobby.auth

import com.bravedroid.jobby.auth.datasource.AuthDataSource
import com.bravedroid.jobby.auth.datasource.TokenProvider
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenRequestDto
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import com.bravedroid.jobby.domain.utils.DomainResult
import dagger.Lazy
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RefreshTokenInterceptor @Inject constructor(
    val authDataSource: Lazy<AuthDataSource>,
    val tokenProvider: TokenProvider,
    val logger: Logger,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)

        logger.log(tag = "OkHttp", msg = " Before Start RunBlocking ")
        logger.log(tag = "OkHttp", msg = " current thread ${Thread.currentThread().name} ")

        runBlocking {
            logger.log("OkHttp", "current thread runBlocking ${Thread.currentThread().name}")
            if (response.code == 401) {
                response.body?.close()
                logger.log(tag = "OkHttp", msg = "response.code == 401")
                logger.log(tag = "OkHttp", msg = " current thread ${Thread.currentThread().name} ")
                authDataSource.get()
                    .refreshToken(RefreshTokenRequestDto(tokenProvider.refreshToken))
                    .catch { e -> logger.log(tag = "OkHttp", t = e) }
                    .collect {
                        logger.log(
                            "OkHttp", " current thread collect${Thread.currentThread().name}"
                        )
                        if (it is DomainResult.Success) {
                            logger.log(tag = "OkHttp", msg = " Success RefreshTokenResponse ")
                            tokenProvider.accessToken = it.data.accessToken
                            val newRequest = chain.request().newBuilder()
                                .removeHeader("Authorization")
                                .addHeader(
                                    "Authorization", "Bearer ${tokenProvider.accessToken}"
                                )
                                .build()
                            response = chain.proceed(newRequest)
                        } else if (it is DomainResult.Error) {
                            logger.log(
                                tag = "OkHttp",
                                msg = "RefreshTokenResponse contains Error",
                                priority = Priority.E
                            )
                        }
                    }
            }
            logger.log(tag = "OkHttp", msg = " End RunBlocking ")
        }
        logger.log(tag = "OkHttp", msg = " after End RunBlocking ")
        return response
    }
}
