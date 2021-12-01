package com.bravedroid.jobby.infrastructure.data.datasource.network.findwork
import com.bravedroid.jobby.domain.entities.Job
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.toResultError
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.toResultSuccess
import com.bravedroid.jobby.domain.utils.ErrorHandler
import com.bravedroid.jobby.infrastructure.data.datasource.network.findwork.dto.JobResponseDto
import com.bravedroid.jobby.infrastructure.data.datasource.network.findwork.dto.JobResponseDto.Companion.toJobList
import com.bravedroid.jobby.infrastructure.data.datasource.network.findwork.service.FindWorkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val findWorkService: FindWorkService,
    private val errorHandler: ErrorHandler,
) {

    fun fetchJobs(): Flow<DomainResult<List<Job>>> = flow {
        val result =
            try {
                val response: JobResponseDto = findWorkService.getJobs()
                response.toJobList().toResultSuccess()
            } catch (e: Exception) {
                errorHandler.handle(e).toResultError()
            }
        emit(result)
    }
}
