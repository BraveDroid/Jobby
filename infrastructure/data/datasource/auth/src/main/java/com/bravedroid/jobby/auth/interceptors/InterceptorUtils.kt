package com.bravedroid.jobby.auth.interceptors

import com.bravedroid.jobby.auth.AuthServiceConstants
import okhttp3.Request

object InterceptorUtils {
    fun isJobbyServerRequest(oldRequest: Request) =
        oldRequest.url.toString().startsWith(AuthServiceConstants.BASE_URL)

    fun Request.Builder.updateHeader(name: String, newValue: String) =
        this.removeHeader(name).addHeader(name, newValue)
}
