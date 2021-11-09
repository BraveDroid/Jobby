package com.bravedroid.jobby.domain.usecases

import com.bravedroid.jobby.domain.entities.Job
import com.bravedroid.jobby.domain.repository.JobsRepository
import com.bravedroid.jobby.domain.utils.Result
import com.bravedroid.jobby.domain.utils.Result.Companion.mapIfSuccess
import kotlinx.coroutines.flow.map

class GetAndroidJobsUseCase constructor(
    private val jobsRepository: JobsRepository,
) {
    operator fun invoke() = jobsRepository.getAndroidJobs().map { result: Result<List<Job>> ->
        result.mapIfSuccess { allJobs: List<Job> ->
            allJobs.groupBy { it.isRemote }.let { map: Map<Boolean, List<Job>> ->
                GetAndroidJobsResponse(
                    remoteJobs = map[true] ?: emptyList(),
                    nonRemoteJobs = map[false] ?: emptyList(),
                )
            }
        }
    }

    data class GetAndroidJobsResponse(
        val remoteJobs: List<Job>,
        val nonRemoteJobs: List<Job>,
    )
}

