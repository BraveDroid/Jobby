package com.bravedroid.jobby.auth.dto.register

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponseDto(
    @SerialName("name")
    val name: String = "",
)
