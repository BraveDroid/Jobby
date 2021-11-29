package com.bravedroid.jobby.auth.dto.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    @SerialName("email")
    val email: String = "",
    @SerialName("name")
    val name: String = ""
)
