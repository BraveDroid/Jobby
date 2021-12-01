package com.bravedroid.jobby.di

import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.logger.NetworkLogger
import com.bravedroid.jobby.logger.TimberLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggerHiltModule {
    @Singleton
    @Binds
    abstract fun bindsLogger(timberLogger: TimberLogger): Logger

    @Singleton
    @Binds
    abstract fun bindsNetworkLogger(timberLogger: TimberLogger): NetworkLogger
}
