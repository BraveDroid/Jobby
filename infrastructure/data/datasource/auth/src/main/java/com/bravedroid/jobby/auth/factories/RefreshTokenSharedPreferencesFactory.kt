package com.bravedroid.jobby.auth.factories

import android.content.SharedPreferences


interface RefreshTokenSharedPreferencesFactory {
    fun create(): SharedPreferences
}
