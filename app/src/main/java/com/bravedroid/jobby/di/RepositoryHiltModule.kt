package com.bravedroid.jobby.di

import com.bravedroid.jobby.domain.repository.JobsRepository
import com.bravedroid.jobby.infrastructure.data.repository.JobsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryHiltModule {
    @Binds
    abstract fun bindsJobsRepository(jobsRepositoryImpl: JobsRepositoryImpl): JobsRepository
}
