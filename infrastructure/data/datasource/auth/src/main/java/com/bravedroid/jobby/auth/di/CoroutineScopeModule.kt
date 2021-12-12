package com.bravedroid.jobby.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CoroutineScopeModule {
    @Singleton // Provide always the same instance
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        // Run this code when providing an instance of CoroutineScope
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Singleton
    @ApplicationScope
    @Provides
    fun providesApplicationCoroutineScope(
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher)

//    @Singleton
//    @Provides
//    fun providesCoroutineScope(
//        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
//    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

}

