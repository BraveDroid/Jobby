package com.bravedroid.jobby.companion.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bravedroid.jobby.auth.AuthenticationImpl
import com.bravedroid.jobby.companion.CoroutineProvider
import com.bravedroid.jobby.companion.PageState
import com.bravedroid.jobby.domain.usecases.GetAndroidJobsUseCase
import com.bravedroid.jobby.domain.usecases.RegisterUserUseCase
import com.bravedroid.jobby.domain.usecases.RegisterUserUseCase.RegistrationResponse
import com.bravedroid.jobby.domain.utils.DomainResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val coroutineProvider: CoroutineProvider,
    private val registerUserUseCase: RegisterUserUseCase,
) : ViewModel() {

    private val _pageStateFlow: MutableStateFlow<PageState<RegistrationResponse>> =
        MutableStateFlow(PageState.Loading)
    val pageStateFlow: StateFlow<PageState<RegistrationResponse>> = _pageStateFlow

    private val _uiEventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow

    fun register(model: RegisterUiModel) {
        coroutineProvider.provideViewModelScope(this).launch {
            registerUserUseCase(model.toRegistrationRequest()).collectLatest {
                when (it) {
                    is DomainResult.Error -> {
                        _pageStateFlow.value = PageState.Error
                        _uiEventFlow.emit(
                            UiEvent.ShowError(
                                "Unknown Error !"
                            )
                        )
                    }
                    is DomainResult.Success -> {
                        _pageStateFlow.value = PageState.Content(it.data)
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
