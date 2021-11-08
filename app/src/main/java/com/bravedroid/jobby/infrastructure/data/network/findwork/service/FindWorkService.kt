package com.bravedroid.jobby.infrastructure.data.network.findwork.service

import com.bravedroid.jobby.infrastructure.data.network.findwork.dto.JobResponseDto
import retrofit2.http.GET

interface FindWorkService {
    @GET
    suspend fun getJobs(): JobResponseDto
}