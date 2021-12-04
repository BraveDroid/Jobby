package com.bravedroid.jobby.di

import com.bravedroid.jobby.auth.AuthenticationImpl
import com.bravedroid.jobby.domain.auth.Authentication
import com.bravedroid.jobby.domain.repository.JobsRepository
import com.bravedroid.jobby.domain.repository.UserProfileRepository
import com.bravedroid.jobby.infrastructure.data.repository.JobsRepositoryImpl
import com.bravedroid.jobby.infrastructure.data.repository.UserProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryHiltModule {
    @Singleton
    @Binds
    abstract fun bindsJobsRepository(jobsRepositoryImpl: JobsRepositoryImpl): JobsRepository

    @Singleton
    @Binds
    abstract fun bindsUserProfileRepository(userProfileRepositoryImpl: UserProfileRepositoryImpl): UserProfileRepository

    @Singleton
    @Binds
    abstract fun bindsAuthentication(authenticationImpl: AuthenticationImpl): Authentication
}
