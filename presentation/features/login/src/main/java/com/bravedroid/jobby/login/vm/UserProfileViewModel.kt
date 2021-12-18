package com.bravedroid.jobby.login.vm

import androidx.lifecycle.ViewModel
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.usecases.GetUserProfileUseCase
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.login.CoroutineProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val coroutineProvider: CoroutineProvider,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logger: Logger,
) : ViewModel() {

    private val _uiEventFlow: MutableStateFlow<UiEvent> =
        MutableStateFlow(UiEvent.UserProfileUiModel("",""))
    val uiEventFlow: StateFlow<UiEvent> = _uiEventFlow

    fun findUser() {
        coroutineProvider.provideViewModelScope(this).launch {
            getUserProfileUseCase().collectLatest {
                when (it) {
                    is DomainResult.Error -> {
                        logger.log("UserProfileViewModel", "${it.errorEntity}")
                        _uiEventFlow.value = UiEvent.ShowError(
                            "Unknown Error !"
                        )
                    }
                    is DomainResult.Success -> {
                        _uiEventFlow.value = UiEvent.UserProfileUiModel(
                            email = it.data.email,
                            name = it.data.name,
                        )
                    }
                }
            }
        }

    }

    sealed class UiEvent {
        data class ShowError(val errorMessage: String) : UiEvent()
        data class UserProfileUiModel(
            val email: String,
            val name: String,
        ): UiEvent()
    }
}
