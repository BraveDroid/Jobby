package com.bravedroid.jobby.auth.datasource

import android.content.SharedPreferences
import javax.inject.Inject

class RefreshTokenStoreImpl @Inject constructor(
 private val sharedPreferences: SharedPreferences,
 private val sharedPreferencesEditor: SharedPreferences.Editor,
):RefreshTokenStore {
    companion object {
        const val PERSISTENCE_FILE_NAME_SECURE = "RefreshTokenStoreFileSecure"
        private const val REFRESH_TOKEN = "REFRESH-TOKEN"
    }

    override fun save(value: String) =
        sharedPreferencesEditor.putString(REFRESH_TOKEN, value).apply()

    override fun get(): String =
        sharedPreferences.getString(REFRESH_TOKEN, "") ?: ""
}
