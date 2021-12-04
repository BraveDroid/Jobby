package com.bravedroid.jobby.auth.datasource

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenProvider @Inject constructor() {
    var accessToken: String = ""
}
