package com.bravedroid.jobby.auth.service

import com.bravedroid.jobby.auth.dto.user.UserResponseDto
import retrofit2.http.GET
import retrofit2.http.Headers


interface UserService {
    @GET("user")
    suspend fun getUser(): UserResponseDto
}