package com.bravedroid.jobby.auth.factories

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.bravedroid.jobby.auth.datasource.RefreshTokenStoreImpl.Companion.PERSISTENCE_FILE_NAME_SECURE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RefreshTokenSharedPreferencesFactoryImpl  @Inject constructor(
    @ApplicationContext private val context: Context,
): RefreshTokenSharedPreferencesFactory() {
    override fun create(): SharedPreferences =
        EncryptedSharedPreferences.create(
            PERSISTENCE_FILE_NAME_SECURE,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
}
