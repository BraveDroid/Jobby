package com.bravedroid.jobby.auth.datasource

import com.bravedroid.jobby.auth.dto.login.LoginRequestDto
import com.bravedroid.jobby.auth.dto.login.LoginResponseDto
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenRequestDto
import com.bravedroid.jobby.auth.dto.refreshtoken.RefreshTokenResponseDto
import com.bravedroid.jobby.auth.dto.register.RegisterRequestDto
import com.bravedroid.jobby.auth.service.AuthService
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.toResultError
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.toResultSuccess
import com.bravedroid.jobby.domain.utils.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val authService: AuthService,
    private val errorHandler: ErrorHandler,
    private val logger: Logger,
    ) {
    fun registerUser(body: RegisterRequestDto) =
        flow {
            val result =
                try {
                    val response = authService.registerUser(body)
                    response.toResultSuccess()
                } catch (e: Exception) {
                    logger.log("AuthDataSource",e.fillInStackTrace())
                    errorHandler.handle(e).toResultError()
                }
            emit(result)
        }

    fun loginUser(body: LoginRequestDto): Flow<DomainResult<LoginResponseDto>> = flow {
        val result =
            try {
                val response = authService.loginUser(body)
                response.toResultSuccess()
            } catch (e: Exception) {
                errorHandler.handle(e).toResultError()
            }
        emit(result)
    }

    fun refreshToken(body: RefreshTokenRequestDto): Flow<DomainResult<RefreshTokenResponseDto>> = flow {
        val result =
            try {
                val response = authService.refreshToken(body)
                response.toResultSuccess()
            } catch (e: Exception) {
                errorHandler.handle(e).toResultError()
            }
        emit(result)
    }
}
