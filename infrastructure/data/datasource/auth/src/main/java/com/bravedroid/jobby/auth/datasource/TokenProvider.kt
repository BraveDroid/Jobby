package com.bravedroid.jobby.auth.datasource

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenProvider @Inject constructor(
    private val refreshTokenStore: RefreshTokenStoreSecure,
) {
    var refreshToken: String
    get()  = refreshTokenStore.get()
    set(value) = refreshTokenStore.save(value)

    var accessToken: String = ""
}
