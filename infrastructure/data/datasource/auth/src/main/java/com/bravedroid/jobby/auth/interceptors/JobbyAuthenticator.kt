package com.bravedroid.jobby.auth.interceptors

import com.bravedroid.jobby.auth.interceptors.InterceptorUtils.updateHeader
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
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class JobbyAuthenticator @Inject constructor(
    private val authDataSource: Lazy<AuthDataSource>,
    private val tokenProvider: TokenProvider,
    private val logger: Logger,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        var newRequest: Request? = null
        if (InterceptorUtils.isJobbyServerRequest(response.request)) {
            newRequest = updateAccessToken(response)
        }
        return newRequest
    }

    @Synchronized
    private fun updateAccessToken(response: Response): Request? = runBlocking {
        var request: Request? = null
        logger.log(
            tag = "OkHttp",
            msg = "current thread collect ${Thread.currentThread().name}",
        )
        authDataSource.get()
            .refreshToken(RefreshTokenRequestDto(tokenProvider.refreshToken))
            .catch { e -> logger.log(tag = "OkHttp", t = e) }
            .collect {
                when (it) {
                    is DomainResult.Success -> {
                        logger.log(
                            tag = "OkHttp",
                            msg = " Success RefreshTokenResponse "
                        )
                        tokenProvider.accessToken = it.data.accessToken
                        request = response.request.newBuilder()
                            .updateHeader(
                                "Authorization",
                                "Bearer ${tokenProvider.accessToken}"
                            )
                            .build()
                    }
                    is DomainResult.Error -> {
                        logger.log(
                            tag = "OkHttp",
                            msg = "RefreshTokenResponse contains Error",
                            priority = Priority.E
                        )
                    }
                }
            }
        logger.log(tag = "OkHttp", msg = " End RunBlocking ")
        request
    }
}
