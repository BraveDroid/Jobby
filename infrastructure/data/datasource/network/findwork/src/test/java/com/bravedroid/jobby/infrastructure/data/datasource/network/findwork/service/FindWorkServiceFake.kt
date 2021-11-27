package com.bravedroid.jobby.infrastructure.data.datasource.network.findwork.service

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@ExperimentalSerializationApi
class FindWorkServiceFake : FindWorkService {
    private val json = Json {
//        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun getJobs(): com.bravedroid.jobby.infrastructure.data.datasource.network.findwork.dto.JobResponseDto {
        val text = this::class.java.getResource("/GET_jobs_from_findwork.json")!!.readText()
        return json.decodeFromString(text)
    }
}
