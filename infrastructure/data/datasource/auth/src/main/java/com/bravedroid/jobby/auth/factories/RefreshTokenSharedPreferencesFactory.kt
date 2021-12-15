package com.bravedroid.jobby.auth.factories

import android.content.SharedPreferences


abstract class RefreshTokenSharedPreferencesFactory {
    abstract fun create(): SharedPreferences
}
