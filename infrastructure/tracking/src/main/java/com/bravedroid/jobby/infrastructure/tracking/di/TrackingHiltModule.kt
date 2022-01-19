package com.bravedroid.jobby.infrastructure.tracking.di

import com.bravedroid.jobby.domain.tracking.EventTracker
import com.bravedroid.jobby.infrastructure.tracking.GoogleTracker
import com.bravedroid.jobby.infrastructure.tracking.JobbyEventTracker
import com.bravedroid.jobby.infrastructure.tracking.LogTracker
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.tracking.JobbyTracker
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [TrackingBuilderHiltModule::class])
@InstallIn(SingletonComponent::class)
abstract class TrackingHiltModule {

    @Singleton
    @GoogleEventTracker
    @Binds
    abstract fun bindsGoogleEventTracker(
        googleTracker: GoogleTracker,
    ): EventTracker

    @Singleton
    @LogEventTracker
    @Binds
    abstract fun bindsLogEventTracker(
        logTracker: LogTracker,
    ): EventTracker

}

@Module
@InstallIn(SingletonComponent::class)
class TrackingBuilderHiltModule {
    @Singleton
    @JobbyTracker
    @Provides
    fun providesJobbyEventTracker(
        @GoogleEventTracker googleTracker: EventTracker,
        @LogEventTracker logTracker: EventTracker,
    ): EventTracker = JobbyEventTracker(
        listOf(
            googleTracker,
            logTracker,
        )
    )
}

