package com.bravedroid.jobby.domain.repository

import com.bravedroid.jobby.domain.entities.Job
import com.bravedroid.jobby.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface JobsRepository {
    fun getAndroidJobs(): Flow<Result<List<Job>>>
}
