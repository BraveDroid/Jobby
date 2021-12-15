package com.bravedroid.jobby.auth.factories

import com.bravedroid.jobby.auth.datasource.RefreshTokenStoreImpl.Companion.PERSISTENCE_FILE_NAME
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RefreshTokenSharedPreferencesFactoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : RefreshTokenSharedPreferencesFactory() {
    override fun create(): SharedPreferences =
        context.getSharedPreferences(PERSISTENCE_FILE_NAME, Context.MODE_PRIVATE)

}
