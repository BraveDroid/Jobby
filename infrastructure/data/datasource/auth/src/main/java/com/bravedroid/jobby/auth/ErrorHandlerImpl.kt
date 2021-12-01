package com.bravedroid.jobby.auth

import com.bravedroid.jobby.domain.entities.ErrorEntity
import com.bravedroid.jobby.domain.utils.ErrorHandler
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class ErrorHandlerImpl @Inject constructor() : ErrorHandler {
    override fun handle(t: Throwable): ErrorEntity =
        when (t) {
            is IOException -> ErrorEntity.Network
            is HttpException -> {
                when (t.code()) {
                    // no cache found in case of no network, thrown by retrofit -> treated as network error
                    HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> ErrorEntity.Network
                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.NotFound
                    HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.AccessDenied
                    HttpURLConnection.HTTP_UNAVAILABLE -> ErrorEntity.ServiceUnavailable
                    else -> ErrorEntity.Unknown
                }
            }
            else -> ErrorEntity.Unknown
        }
}
