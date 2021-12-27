package com.bravedroid.jobby.login.vm

import androidx.lifecycle.ViewModel
 import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.usecases.LoginUserUseCase
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.login.CoroutineProvider
import com.bravedroid.jobby.login.FormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
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

    fun login(model: LoginUiModel) {
        coroutineProvider.provideViewModelScope(this).launch {
            loginUserUseCase(model.toLoginRequest()).collectLatest {
                when (it) {
                    is DomainResult.Error -> {
                        logger.log("LoginViewModel", "${it.errorEntity}")
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

    data class LoginUiModel(
        val email: String,
        val password: String,
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
