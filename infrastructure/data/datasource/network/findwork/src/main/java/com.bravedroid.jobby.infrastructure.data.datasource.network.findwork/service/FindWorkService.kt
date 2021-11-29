package com.bravedroid.jobby.infrastructure.data.datasource.network.findwork.service

import com.bravedroid.jobby.infrastructure.data.datasource.network.findwork.FindWorkConstants.API_KEY
import com.bravedroid.jobby.infrastructure.data.datasource.network.findwork.dto.JobResponseDto
import retrofit2.http.GET
import retrofit2.http.Headers

interface FindWorkService {
    @GET("?search=android+kotlin+java&source=&location=&company_num_employees=&employment_type=&order_by=date")
    @Headers("Authorization: Token $API_KEY")
    suspend fun getJobs(): JobResponseDto
}
