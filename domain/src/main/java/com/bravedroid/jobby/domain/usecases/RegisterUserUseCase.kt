package com.bravedroid.jobby.domain.usecases

import com.bravedroid.jobby.domain.auth.Authentication
import com.bravedroid.jobby.domain.utils.DomainResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val authentication: Authentication,
) {
    operator fun invoke(registrationRequest: RegistrationRequest): Flow<DomainResult<RegistrationResponse>> =
        authentication.register(registrationRequest)

    data class RegistrationRequest(
        val email: String,
        val name: String,
        val password: String,
    )

    sealed interface RegistrationResponse {
        object Registered : RegistrationResponse
        class NotRegisteredException(t: Throwable) : RegistrationResponse, RuntimeException(t)
    }
}
