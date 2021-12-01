package com.bravedroid.jobby.auth.di

import javax.inject.Inject

class TokenProvider @Inject constructor() {
    var accessToken: String = ""
}
