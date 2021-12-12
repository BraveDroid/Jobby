package com.bravedroid.jobby.auth.datasource

import android.content.SharedPreferences
import com.bravedroid.jobby.auth.di.RefreshTokenSharedPreferences
import com.bravedroid.jobby.auth.di.RefreshTokenSharedPreferencesEditor
import javax.inject.Inject

class RefreshTokenStore @Inject constructor(
    @RefreshTokenSharedPreferences private val sharedPreferences: SharedPreferences,
    @RefreshTokenSharedPreferencesEditor private val sharedPreferencesEditor: SharedPreferences.Editor,
) {
    companion object {
        const val PERSISTENCE_FILE_NAME = "RefreshTokenStoreFile"
        private const val REFRESH_TOKEN = "REFRESH-TOKEN"
    }

    fun save(value: String) =
        sharedPreferencesEditor.putString(REFRESH_TOKEN, value).apply()

    fun get(): String =
        sharedPreferences.getString(REFRESH_TOKEN, "") ?: ""
}
