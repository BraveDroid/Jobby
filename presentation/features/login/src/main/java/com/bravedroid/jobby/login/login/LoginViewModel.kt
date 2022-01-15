package com.bravedroid.jobby.login.login

import androidx.lifecycle.ViewModel
import com.bravedroid.jobby.core.CoroutineProvider
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import com.bravedroid.jobby.domain.usecases.LoginUserUseCase
import com.bravedroid.jobby.domain.utils.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val coroutineProvider: CoroutineProvider,
    private val loginUserUseCase: LoginUserUseCase,
    private val logger: Logger,
) : ViewModel() {
    private val _uiEventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow

    private val _loginUiModelStateFlow: MutableStateFlow<LoginUiState> =
        MutableStateFlow(LoginUiState("", "", false))
    val loginUiModelStateFlow: StateFlow<LoginUiState> = _loginUiModelStateFlow

    fun saveLoginState(loginUiState: LoginUiState) {
        _loginUiModelStateFlow.value = loginUiState
    }

    fun login(model: LoginUiModel) {
        coroutineProvider.provideViewModelScope(this).launch {
            loginUserUseCase(model.toLoginRequest()).collectLatest {
                when (it) {
                    is DomainResult.Error -> {
                        logger.log(
                            tag = "LoginViewModel",
                            priority = Priority.E,
                            msg = "${it.errorEntity}"
                        )
                        _uiEventFlow.emit(
                            UiEvent.ShowError(
                                "Unknown Error !"
                            )
                        )
                    }
                    is DomainResult.Success -> {
                        if (it.data == LoginUserUseCase.LoginResponse.LoggedIn)
                            _uiEventFlow.emit(UiEvent.NavigationToUserProfile)
                    }
                }
            }
        }
    }

    data class LoginUiState(
        val email: String,
        val password: String,
        val isValid: Boolean,
    )

    private fun LoginUiModel.toLoginRequest() = LoginUserUseCase.LoginRequest(
        email,
        password,
    )

    sealed class UiEvent {
        data class ShowError(val errorMessage: String) : UiEvent()
        object NavigationToUserProfile : UiEvent()
    }

}
