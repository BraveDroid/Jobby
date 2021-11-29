package com.bravedroid.jobby.auth

import com.bravedroid.jobby.auth.dto.login.LoginRequestDto
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenRequestDto
import com.bravedroid.jobby.auth.dto.register.RegisterRequestDto
import com.bravedroid.jobby.auth.service.AuthService
import com.bravedroid.jobby.domain.utils.Result.Companion.toResultErrorUnknown
import com.bravedroid.jobby.domain.utils.Result.Companion.toResultSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val authService: AuthService,
) {
    fun registerUser(body: RegisterRequestDto) =
        flow {
            val result =
                try {
                    val response = authService.registerUser(body)
                    response.toResultSuccess()
                } catch (e: Exception) {
                    when (e) {
//                    is RuntimeException ->
                        else -> {
                            e.toResultErrorUnknown()
                        }
                    }
                }
            emit(result)
        }

    fun loginUser(body: LoginRequestDto) = flow {
        val result =
            try {
                val response = authService.loginUser(body)
                response.toResultSuccess()
            } catch (e: Exception) {
                when (e) {
//                    is RuntimeException ->
                    else -> {
                        e.toResultErrorUnknown()
                    }
                }
            }
        emit(result)
    }

    fun refreshToken(body: RefreshTokenRequestDto) = flow {
        val result =
            try {
                val response = authService.refreshToken(body)
                response.toResultSuccess()
            } catch (e: Exception) {
                when (e) {
//                    is RuntimeException ->
                    else -> {
                        e.toResultErrorUnknown()
                    }
                }
            }
        emit(result)
    }
}
