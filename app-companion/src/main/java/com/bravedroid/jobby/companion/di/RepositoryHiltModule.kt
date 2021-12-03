package com.bravedroid.jobby.companion.di

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

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryHiltModule {
    @Binds
    abstract fun bindsJobsRepository(jobsRepositoryImpl: JobsRepositoryImpl): JobsRepository

    @Binds
    abstract fun bindsUserProfileRepository(userProfileRepositoryImpl: UserProfileRepositoryImpl): UserProfileRepository

    @Binds
    abstract fun bindsAuthentication(authenticationImpl: AuthenticationImpl): Authentication
}
