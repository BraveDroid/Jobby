package com.bravedroid.jobby.auth

import com.bravedroid.jobby.auth.datasource.TokenProvider
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        var newRequest: Request = oldRequest
        if (isJobbyServerRequest(oldRequest)) {
            newRequest = oldRequest.newBuilder()
                .addHeader("Authorization", "Bearer ${tokenProvider.accessToken}")
                .build()
        }
        return chain.proceed(newRequest)
    }

    private fun isJobbyServerRequest(oldRequest: Request) =
        oldRequest.url.toString().startsWith(AuthServiceConstants.BASE_URL)
}
