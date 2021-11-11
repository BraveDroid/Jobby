package com.bravedroid.jobby.domain.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

class CoroutineProvider @Inject constructor() {
    fun provideViewModelContext(viewModel: ViewModel) = viewModel.viewModelScope.coroutineContext
    fun provideViewModelScope(viewModel: ViewModel) = viewModel.viewModelScope

}
