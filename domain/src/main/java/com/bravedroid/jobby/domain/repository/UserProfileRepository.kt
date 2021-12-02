package com.bravedroid.jobby.domain.repository

import com.bravedroid.jobby.domain.entities.Profile
import com.bravedroid.jobby.domain.usecases.GetUserProfileUseCase.UserProfileRequest
import com.bravedroid.jobby.domain.utils.DomainResult
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun getProfileUser(userProfileRequest: UserProfileRequest): Flow<DomainResult<Profile>>
}
