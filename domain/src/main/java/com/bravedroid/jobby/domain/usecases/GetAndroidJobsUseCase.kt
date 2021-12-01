package com.bravedroid.jobby.domain.usecases

import com.bravedroid.jobby.domain.entities.Job
import com.bravedroid.jobby.domain.repository.JobsRepository
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.toResultError
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.toResultSuccess
import com.bravedroid.jobby.domain.utils.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAndroidJobsUseCase @Inject constructor(
    private val jobsRepository: JobsRepository,
    private val errorHandler: ErrorHandler,
) {
    operator fun invoke(): Flow<DomainResult<GetAndroidJobsResponse>> =
        jobsRepository.getAndroidJobs()
            .catch { e ->
                errorHandler.handle(e).toResultError()
            }
            .map { domainResult: DomainResult<List<Job>> ->
                domainResult as DomainResult.Success
                val allJobs: List<Job> = domainResult.data
                allJobs.groupBy { it.isRemote }.let { map: Map<Boolean, List<Job>> ->
                    GetAndroidJobsResponse(
                        remoteJobs = map[true] ?: emptyList(),
                        nonRemoteJobs = map[false] ?: emptyList(),
                    ).toResultSuccess()
                }
            }


    data class GetAndroidJobsResponse(
        val remoteJobs: List<Job>,
        val nonRemoteJobs: List<Job>,
    )

}
