package com.bravedroid.jobby.companion.vm

import androidx.lifecycle.ViewModel
import com.bravedroid.jobby.companion.CoroutineProvider
import com.bravedroid.jobby.companion.vm.FlowExt.combineOn
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import com.bravedroid.jobby.domain.usecases.RegisterUserUseCase
import com.bravedroid.jobby.domain.usecases.RegisterUserUseCase.RegistrationResponse
import com.bravedroid.jobby.domain.utils.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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

    fun validateRegisterForm(
        nameSharedFlow: Flow<String>,
        emailSharedFlow: Flow<String>,
        passwordSharedFlow: Flow<String>
    ): Flow<Boolean> = combineOn(
        Dispatchers.Default,
        nameSharedFlow,
        emailSharedFlow,
        passwordSharedFlow
    ) { name, email, password ->
        logger.log(
            tag = "RegisterViewModel",
            msg = "name: $name, email: $email, password: $password",
            priority = Priority.V
        )
        validateForm(name, email, password)
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        val isValidName = name.isNotBlank()
        val isValidEmail = email.isNotBlank() && email.contains("@")
        val isValidPassword = password.isNotBlank() && password.length >= 8
        return isValidName && isValidEmail && isValidPassword
    }

    sealed class UiEvent {
        data class ShowError(val errorMessage: String) : UiEvent()
        object NavigationToLoginScreen : UiEvent()
    }
}

object FlowExt {
    fun <T1, T2, T3, R> combineOn(
        dispatcher: CoroutineDispatcher,
        flow: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        transform: suspend (T1, T2, T3) -> R,
    ): Flow<R> = combine(
        flow.flowOn(dispatcher),
        flow2.flowOn(dispatcher),
        flow3.flowOn(dispatcher),
        transform
    )
}
