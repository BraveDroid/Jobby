package com.bravedroid.jobby.auth.service

import com.bravedroid.jobby.auth.dto.login.LoginRequestDto
import com.bravedroid.jobby.auth.dto.login.LoginResponseDto
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenRequestDto
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenResponseDto
import com.bravedroid.jobby.auth.dto.register.RegisterRequestDto
import com.bravedroid.jobby.auth.dto.register.RegisterResponseDto
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@ExperimentalSerializationApi
class AuthServiceFake : AuthService {
    private val json = Json {
//        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun registerUser(body: RegisterRequestDto): RegisterResponseDto {
        val text = this::class.java.getResource("/POST_register_user.json")!!.readText()
        return json.decodeFromString(text)
    }

    override suspend fun loginUser(body: LoginRequestDto): LoginResponseDto {
        val text = this::class.java.getResource("/POST_login_user.json")!!.readText()
        return json.decodeFromString(text)
    }


    override suspend fun refreshToken(body: RefreshTokenRequestDto): RefreshTokenResponseDto {
        val text = this::class.java.getResource("/POST_refresh_token.json")!!.readText()
        return json.decodeFromString(text)
    }
}
