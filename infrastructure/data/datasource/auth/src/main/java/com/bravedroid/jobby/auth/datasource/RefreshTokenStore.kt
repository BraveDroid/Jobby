package com.bravedroid.jobby.auth.datasource

interface RefreshTokenStore {
    fun save(value: String)
    fun get(): String
}
