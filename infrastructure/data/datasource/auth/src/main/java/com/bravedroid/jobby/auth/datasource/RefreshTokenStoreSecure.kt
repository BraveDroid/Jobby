package com.bravedroid.jobby.auth.datasource

import android.content.SharedPreferences
import com.bravedroid.jobby.auth.di.RefreshTokenSharedPreferences
import com.bravedroid.jobby.auth.di.RefreshTokenSharedPreferencesEditor
import com.bravedroid.jobby.auth.di.RefreshTokenSharedPreferencesEditorSecure
import com.bravedroid.jobby.auth.di.RefreshTokenSharedPreferencesSecure
import javax.inject.Inject

class RefreshTokenStoreSecure @Inject constructor(
    @RefreshTokenSharedPreferencesSecure private val sharedPreferences: SharedPreferences,
    @RefreshTokenSharedPreferencesEditorSecure private val sharedPreferencesEditor: SharedPreferences.Editor,
) {
    companion object {
        const val PERSISTENCE_FILE_NAME_SECURE = "RefreshTokenStoreFileSecure"
        private const val REFRESH_TOKEN = "REFRESH-TOKEN"
    }

    fun save(value: String) =
        sharedPreferencesEditor.putString(REFRESH_TOKEN, value).apply()

    fun get(): String =
        sharedPreferences.getString(REFRESH_TOKEN, "") ?: ""
}
