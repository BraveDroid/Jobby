package com.bravedroid.jobby.infrastructure.tracking.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GoogleEventTracker

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LogEventTracker

