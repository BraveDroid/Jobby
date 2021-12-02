package com.bravedroid.jobby.auth

import com.bravedroid.jobby.auth.dto.user.UserResponseDto
import com.bravedroid.jobby.auth.service.UserService
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.toResultError
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.toResultSuccess
import com.bravedroid.jobby.domain.utils.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val userService: UserService,
    private val errorHandler: ErrorHandler,
    private val logger: Logger,
) {
    fun getProfileUser() : Flow<DomainResult<UserResponseDto>> {
        return flow {
            val result =
                try {
                    val response = userService.getUser()
                    response.toResultSuccess()
                } catch (e: Exception) {
                    logger.log("UserDataSource", e.fillInStackTrace())
                    errorHandler.handle(e).toResultError()
                }
            emit(result)
        }
    }
}
