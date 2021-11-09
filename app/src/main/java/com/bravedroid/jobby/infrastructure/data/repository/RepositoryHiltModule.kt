package com.bravedroid.jobby.infrastructure.data.repository

import com.bravedroid.jobby.domain.repository.JobsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryHiltModule {
    @Binds
    abstract fun binds(jobsRepositoryImpl: JobsRepositoryImpl): JobsRepository
}
