package com.bravedroid.jobby.auth.service

import com.bravedroid.jobby.auth.dto.login.LoginRequestDto
import com.bravedroid.jobby.auth.dto.login.LoginResponseDto
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenRequestDto
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenResponseDto
import com.bravedroid.jobby.auth.dto.register.RegisterRequestDto
import com.bravedroid.jobby.auth.dto.register.RegisterResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("register")
    suspend fun registerUser(@Body body: RegisterRequestDto): RegisterResponseDto

    @POST("login")
    suspend fun loginUser(@Body body: LoginRequestDto): LoginResponseDto

    @POST("refresh-token")
    suspend fun refreshToken(@Body body: RefreshTokenRequestDto): RefreshTokenResponseDto

}
