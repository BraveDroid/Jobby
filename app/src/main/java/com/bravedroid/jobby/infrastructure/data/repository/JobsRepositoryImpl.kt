package com.bravedroid.jobby.infrastructure.data.repository

import com.bravedroid.jobby.domain.repository.JobsRepository
import com.bravedroid.jobby.infrastructure.data.datasource.network.findwork.NetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobsRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
) : JobsRepository {
    override fun getAndroidJobs() =
        networkDataSource.fetchJobs()
}
