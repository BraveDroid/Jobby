package com.bravedroid.jobby.auth.factories

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RefreshTokenSharedPreferencesFactoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : RefreshTokenSharedPreferencesFactory {

    private companion object {
        private const val REFRESH_TOKEN_STORE_FILE = "RefreshTokenStoreFile"
    }

    override fun create(): SharedPreferences =
        context.getSharedPreferences(REFRESH_TOKEN_STORE_FILE, Context.MODE_PRIVATE)
}
