package com.bravedroid.jobby.companion.app

import android.app.Application
import com.bravedroid.jobby.domain.log.Logger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    internal lateinit var logger: Logger

    override fun onCreate() {
        super.onCreate()
        logger.init()
    }
}
