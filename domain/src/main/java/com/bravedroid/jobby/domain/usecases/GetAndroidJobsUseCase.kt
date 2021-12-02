package com.bravedroid.jobby.domain.usecases

import com.bravedroid.jobby.domain.entities.Job
import com.bravedroid.jobby.domain.repository.JobsRepository
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.mapToResultSuccessOrKeepSameResultError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAndroidJobsUseCase @Inject constructor(
    private val jobsRepository: JobsRepository,
) {
    operator fun invoke(): Flow<DomainResult<GetAndroidJobsResponse>> =
        jobsRepository.getAndroidJobs()
            .map {
                it.mapToResultSuccessOrKeepSameResultError { allJobs ->
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
