package com.bravedroid.jobby.auth.dto.register

import com.bravedroid.jobby.domain.usecases.RegisterUserUseCase.RegistrationRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    @SerialName("email")
    val email: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("password")
    val password: String = "",
)

fun RegistrationRequest.toRegisterRequestDto(): RegisterRequestDto =
    RegisterRequestDto(
        email = this.email,
        name = this.name,
        password = this.password,
    )
