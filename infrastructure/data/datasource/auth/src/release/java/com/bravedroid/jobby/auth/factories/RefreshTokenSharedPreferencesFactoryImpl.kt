package com.bravedroid.jobby.auth.factories

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RefreshTokenSharedPreferencesFactoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : RefreshTokenSharedPreferencesFactory {

    private companion object {
        private const val REFRESH_TOKEN_STORE_FILE_SECURE = "RefreshTokenStoreFileSecure"
    }

    override fun create(): SharedPreferences =
        EncryptedSharedPreferences.create(
            REFRESH_TOKEN_STORE_FILE_SECURE,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
}
