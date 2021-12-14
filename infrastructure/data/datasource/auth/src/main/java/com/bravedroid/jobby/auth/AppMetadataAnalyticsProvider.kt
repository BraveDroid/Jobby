package com.bravedroid.jobby.auth

import android.content.Context
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppMetadataAnalyticsProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private companion object {
        private const val APP_VERSION = "JOBBY-App-Version"
        private const val DEVICE_MODEL = "JOBBY-Device-Model"
        private const val DEVICE_PLATFORM = "JOBBY-Device-Platform"
        private const val OS_VERSION = "JOBBY-Device-OS-Version"
    }

    fun provideMetaData(): Map<String, String> {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val version = packageInfo.versionName

        return mapOf(
            APP_VERSION to version,
            DEVICE_MODEL to Build.VERSION.SDK_INT.toString(),
            DEVICE_PLATFORM to Build.MODEL,
            OS_VERSION to "android",
        )
    }
}
