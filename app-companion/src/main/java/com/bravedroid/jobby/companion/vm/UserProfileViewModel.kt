package com.bravedroid.jobby.companion.vm

import androidx.lifecycle.ViewModel
import com.bravedroid.jobby.domain.usecases.GetUserProfileUseCase
import com.bravedroid.jobby.domain.usecases.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
) : ViewModel() {

    fun findUser(model: UserProfileUiModel) {
        getUserProfileUseCase(model.toUserProfileRequest())
    }


    data class UserProfileUiModel(
        val email: String,
        val name: String,
        val password: String,
    )

    fun UserProfileUiModel.toUserProfileRequest() = GetUserProfileUseCase.UserProfileRequest(
        email,
        name,
        password,
    )
}
