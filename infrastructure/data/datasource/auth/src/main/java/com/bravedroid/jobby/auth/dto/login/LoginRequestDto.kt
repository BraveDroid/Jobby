package com.bravedroid.jobby.auth.dto.login


import com.bravedroid.jobby.domain.usecases.LoginUserUseCase.LoginRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    @SerialName("email")
    val email: String = "",
    @SerialName("password")
    val password: String = "",
)

fun LoginRequest.toLoginRequestDto(): LoginRequestDto =
    LoginRequestDto(
        email = this.email,
        password = this.password,
    )
