package com.bravedroid.jobby.infrastructure.data.datasource.network.findwork
import com.bravedroid.jobby.domain.utils.Result.Companion.toResultErrorUnknown
import com.bravedroid.jobby.domain.utils.Result.Companion.toResultSuccess
import com.bravedroid.jobby.infrastructure.data.datasource.network.findwork.dto.JobResponseDto
import com.bravedroid.jobby.infrastructure.data.datasource.network.findwork.dto.JobResponseDto.Companion.toJobList
import com.bravedroid.jobby.infrastructure.data.datasource.network.findwork.service.FindWorkService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val findWorkService: FindWorkService,
) {

    fun fetchJobs() = flow {
        val result =
            try {
                val response: JobResponseDto = findWorkService.getJobs()
                response.toJobList().toResultSuccess()
            } catch (e: Exception) {
                // we catch known errors like (bad parsing, offline, 404..)
                when (e) {
//                    is RuntimeException ->
                }
                e.toResultErrorUnknown()
            }
        emit(result)
    }
}
