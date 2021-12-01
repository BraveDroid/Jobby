package com.bravedroid.jobby.auth.dto.user


import com.bravedroid.jobby.domain.entities.Profile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    @SerialName("email")
    val email: String = "",
    @SerialName("name")
    val name: String = ""
) {
    companion object {
        fun UserResponseDto.toUser(): Profile {
            return Profile(
                email,
                name,
            )
        }
    }
}
