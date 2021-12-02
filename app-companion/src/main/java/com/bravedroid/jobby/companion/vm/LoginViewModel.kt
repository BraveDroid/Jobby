package com.bravedroid.jobby.companion.vm

import androidx.lifecycle.ViewModel
import com.bravedroid.jobby.domain.usecases.LoginUserUseCase
import com.bravedroid.jobby.domain.usecases.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
) : ViewModel() {


    fun login(model: LoginUiModel) {
        loginUserUseCase(model.toLoginRequest())
    }

    data class LoginUiModel(
        val email: String,
        val password: String,
    )

    private fun LoginUiModel.toLoginRequest() = LoginUserUseCase.LoginRequest(
        email,
        password,
    )
}
