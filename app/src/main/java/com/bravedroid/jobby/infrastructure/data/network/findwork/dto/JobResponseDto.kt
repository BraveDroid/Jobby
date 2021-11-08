package com.bravedroid.jobby.infrastructure.data.network.findwork.dto

import com.bravedroid.jobby.infrastructure.data.network.findwork.dto.ResultDto.Companion.toJob
import com.bravedroid.jobby.domain.entities.Job
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobResponseDto(
    @SerialName("count")
    val count: Int = 0,
    @SerialName("next")
    val next: String = "",
    @SerialName("previous")
    val previous: String = "",
    @SerialName("results")
    val results: List<ResultDto> = listOf()
) {
    companion object {
        fun JobResponseDto.toJobList(): List<Job> {
            val jobs = mutableListOf<Job>()
            for (i in this.results) {
                jobs.add(i.toJob())
            }
            return jobs
        }
    }
}
