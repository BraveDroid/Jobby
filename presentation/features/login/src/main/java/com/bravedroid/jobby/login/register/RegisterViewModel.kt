package com.bravedroid.jobby.login.register

import androidx.lifecycle.ViewModel
import com.bravedroid.jobby.core.CoroutineProvider
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.usecases.RegisterUserUseCase
import com.bravedroid.jobby.domain.usecases.RegisterUserUseCase.RegistrationResponse
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.login.util.FormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val coroutineProvider: CoroutineProvider,
    private val registerUserUseCase: RegisterUserUseCase,
    private val logger: Logger,
    private val formValidator: FormValidator,
) : ViewModel() {

    private val _uiEventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow

    private val _registerUiModelStateFlow: MutableStateFlow<RegisterUiState> =
        MutableStateFlow(RegisterUiState("", "", "", false))
    val registerUiModelStateFlow: StateFlow<RegisterUiState> = _registerUiModelStateFlow

    fun saveRegisterState(registerUiState: RegisterUiState) {
        _registerUiModelStateFlow.value = registerUiState
    }

    fun register(model: RegisterUiModel) {
        coroutineProvider.provideViewModelScope(this).launch {
            registerUserUseCase(model.toRegistrationRequest()).collectLatest {
                when (it) {
                    is DomainResult.Error -> {
                        logger.log("RegisterViewModel", "${it.errorEntity}")
                        _uiEventFlow.emit(
                            UiEvent.ShowError(
                                "Unknown Error !"
                            )
                        )
                    }
                    is DomainResult.Success -> {
                        if (it.data == RegistrationResponse.Registered)
                            _uiEventFlow.emit(UiEvent.NavigationToLoginScreen)
                    }
                }
            }
        }
    }

    data class RegisterUiState(
        val email: String,
        val name: String,
        val password: String,
        val isValid: Boolean,
    )

    private fun RegisterUiModel.toRegistrationRequest() = RegisterUserUseCase.RegistrationRequest(
        email,
        name,
        password,
    )

    fun validateRegisterForm(
        nameStateFlow: MutableStateFlow<String>,
        emailStateFlow: MutableStateFlow<String>,
        passwordStateFlow: MutableStateFlow<String>
    ) =
        formValidator.validateRegisterForm(nameStateFlow, emailStateFlow, passwordStateFlow)

    sealed class UiEvent {
        data class ShowError(val errorMessage: String) : UiEvent()
        object NavigationToLoginScreen : UiEvent()
    }
}

