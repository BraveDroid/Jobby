package com.bravedroid.jobby.auth.interceptors

import com.bravedroid.jobby.auth.interceptors.InterceptorUtils.isJobbyServerRequest
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
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject

@Deprecated(
    message = "Use JobbyAuthenticator",
    replaceWith = ReplaceWith(
        expression = "JobbyAuthenticator",
        imports = ["com.bravedroid.jobby.auth.interceptors"],
    ),
    level = DeprecationLevel.WARNING
)
class RefreshTokenInterceptor @Inject constructor(
    private val authDataSource: Lazy<AuthDataSource>,
    private val tokenProvider: TokenProvider,
    private val logger: Logger,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        if (shouldAuthenticate(request, response)) {
            updateResponse(response, chain)?.let {
                response = it
            }
        }
        return response
    }

    @Synchronized
    private fun updateResponse(oldResponse: Response, chain: Interceptor.Chain) = runBlocking {
        oldResponse.close()
        var newResponse: Response? = null
        logger.log(
            tag = "OkHttp",
            msg = "current thread ${Thread.currentThread().name} "
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
                        val newRequest = chain.request().newBuilder()
                            .updateHeader(
                                "Authorization",
                                "Bearer ${tokenProvider.accessToken}"
                            )
                            .build()
                        newResponse = chain.proceed(newRequest)
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
        newResponse
    }

    private fun shouldAuthenticate(request: Request, response: Response) =
        isJobbyServerRequest(request) && (response.code == HttpURLConnection.HTTP_UNAUTHORIZED).also { should ->
            if (should) logger.log(tag = "OkHttp", msg = "response.code == 401")
        }
}
