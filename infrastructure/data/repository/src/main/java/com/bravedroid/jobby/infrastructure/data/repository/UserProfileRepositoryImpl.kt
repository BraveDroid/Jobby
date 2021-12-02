package com.bravedroid.jobby.infrastructure.data.repository

import com.bravedroid.jobby.auth.UserDataSource
import com.bravedroid.jobby.domain.entities.Profile
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.repository.UserProfileRepository
import com.bravedroid.jobby.domain.usecases.GetUserProfileUseCase.UserProfileRequest
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.getDataOrNull
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.mapToResultSuccessOrKeepSameResultError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val logger: Logger,
) : UserProfileRepository {
    override fun getProfileUser(userProfileRequest: UserProfileRequest): Flow<DomainResult<Profile>> {
        return userDataSource.getProfileUser()
            .map { result ->
                result.mapToResultSuccessOrKeepSameResultError {
                    logger.log(
                        tag = "UserProfileRepositoryImpl",
                        msg = "${(result.getDataOrNull() ?: "No Data")}"
                    )
                    Profile(
                        email = it.email,
                        name = it.name,
                    )
                }
            }
    }
}
