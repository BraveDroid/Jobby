package com.bravedroid.jobby.companion.vm

import androidx.lifecycle.ViewModel
import com.bravedroid.jobby.companion.CoroutineProvider
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.usecases.RegisterUserUseCase
import com.bravedroid.jobby.domain.usecases.RegisterUserUseCase.RegistrationResponse
import com.bravedroid.jobby.domain.utils.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val coroutineProvider: CoroutineProvider,
    private val registerUserUseCase: RegisterUserUseCase,
    private val logger: Logger,
    ) : ViewModel() {

    private val _uiEventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow

    fun register(model: RegisterUiModel) {
        coroutineProvider.provideViewModelScope(this).launch {
            registerUserUseCase(model.toRegistrationRequest()).collectLatest {
                when (it) {
                    is DomainResult.Error -> {
                        logger.log("RegisterViewModel","${it.errorEntity}")
                        _uiEventFlow.emit(
                            UiEvent.ShowError(
                                "Unknown Error !"
                            )
                        )
                    }
                    is DomainResult.Success -> {
                    if (it.data== RegistrationResponse.Registered)
                        _uiEventFlow.emit(UiEvent.NavigationToLoginScreen)
                    }
                }
            }
        }
    }

    data class RegisterUiModel(
        val email: String,
        val name: String,
        val password: String,
    )

    private fun RegisterUiModel.toRegistrationRequest() = RegisterUserUseCase.RegistrationRequest(
        email,
        name,
        password,
    )

    sealed class UiEvent {
        data class ShowError(val errorMessage: String) : UiEvent()
        object NavigationToLoginScreen : UiEvent()
    }
}
