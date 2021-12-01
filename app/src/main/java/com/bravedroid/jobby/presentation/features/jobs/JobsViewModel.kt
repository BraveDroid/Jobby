package com.bravedroid.jobby.presentation.features.jobs

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.bravedroid.jobby.di.CoroutineProvider
import com.bravedroid.jobby.domain.entities.ErrorEntity
import com.bravedroid.jobby.domain.usecases.GetAndroidJobsUseCase
import com.bravedroid.jobby.domain.usecases.GetAndroidJobsUseCase.GetAndroidJobsResponse
import com.bravedroid.jobby.domain.utils.DomainResult
import com.bravedroid.jobby.domain.utils.DomainResult.Companion.toResultError
import com.bravedroid.jobby.presentation.util.PageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobsViewModel @Inject constructor(
    private val coroutineProvider: CoroutineProvider,
    private val savedStateHandle: SavedStateHandle,
    private val getAndroidJobsUseCase: GetAndroidJobsUseCase,
) : ViewModel() {


    private val _pageStateFlow: MutableStateFlow<PageState<GetAndroidJobsResponse>> =
        MutableStateFlow(PageState.Loading)
    val pageStateFlow: StateFlow<PageState<GetAndroidJobsResponse>> = _pageStateFlow

    private val _uiEventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow

    init {
        loadJobs()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
     fun loadJobs() {
        coroutineProvider.provideViewModelScope(this).launch {
            getAndroidJobsUseCase().catch { t ->
                emit(ErrorEntity.Unknown.toResultError())
            }.collectLatest {
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
                        _uiEventFlow.emit(UiEvent.ContentLoaded)
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowError(val errorMessage: String) : UiEvent()
        object ContentLoaded : UiEvent()
    }


}
