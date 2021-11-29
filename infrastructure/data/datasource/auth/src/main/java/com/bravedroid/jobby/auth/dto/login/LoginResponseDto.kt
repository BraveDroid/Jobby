package com.bravedroid.jobby.auth.dto.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    @SerialName("accessToken")
    val accessToken: String = "",
    @SerialName("refreshToken")
    val refreshToken: String = "",
    @SerialName("tokenType")
    val tokenType: String = ""
)
