package com.bravedroid.jobby.domain.usecases

import com.bravedroid.jobby.domain.auth.Authentication
import com.bravedroid.jobby.domain.utils.DomainResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val authentication: Authentication,
) {
    operator fun invoke(loginRequest: LoginRequest): Flow<DomainResult<LoginResponse>> =
        authentication.login(loginRequest)

    data class LoginRequest(
        val email: String,
        val password: String,
    )

    sealed interface LoginResponse {
        object LoggedIn : LoginResponse
    }
}
