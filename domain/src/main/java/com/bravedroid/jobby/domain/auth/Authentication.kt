package com.bravedroid.jobby.domain.auth

import com.bravedroid.jobby.domain.usecases.LoginUserUseCase
import com.bravedroid.jobby.domain.usecases.LoginUserUseCase.*
import com.bravedroid.jobby.domain.usecases.RegisterUserUseCase.RegistrationRequest
import com.bravedroid.jobby.domain.usecases.RegisterUserUseCase.RegistrationResponse
import com.bravedroid.jobby.domain.utils.DomainResult
import kotlinx.coroutines.flow.Flow

interface Authentication {
    fun register(registrationRequest: RegistrationRequest): Flow<DomainResult<RegistrationResponse>>
    fun login(loginRequest: LoginRequest): Flow<DomainResult<LoginResponse>>
    fun isUserLoggedIn(): Flow<Boolean>
}
