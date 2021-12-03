package com.bravedroid.jobby.companion.di

import com.bravedroid.jobby.auth.ErrorHandlerImpl
import com.bravedroid.jobby.domain.utils.ErrorHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorHandlerHiltModule {
    @Binds
    abstract fun bindsErrorHandler(errorHandler: ErrorHandlerImpl): ErrorHandler
}
