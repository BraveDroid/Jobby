package com.bravedroid.jobby.auth.dto.refreshtoken

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponseDto(
    @SerialName("accessToken")
    val accessToken: String = "",
    @SerialName("tokenType")
    val tokenType: String = "",
)
