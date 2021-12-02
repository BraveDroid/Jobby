package com.bravedroid.jobby.domain.usecases

import com.bravedroid.jobby.domain.entities.Profile
import com.bravedroid.jobby.domain.repository.UserProfileRepository
import com.bravedroid.jobby.domain.utils.DomainResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
){
    operator fun invoke(userProfileRequest: UserProfileRequest): Flow<DomainResult<Profile>> =
        userProfileRepository.getProfileUser(userProfileRequest)

    data class UserProfileRequest(
        val email: String,
        val name: String,
        val password: String,
    )

}
