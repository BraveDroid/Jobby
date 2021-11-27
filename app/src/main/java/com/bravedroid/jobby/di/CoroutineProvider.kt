package com.bravedroid.jobby.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

class CoroutineProvider @Inject constructor() {
    fun provideViewModelContext(viewModel: ViewModel) = viewModel.viewModelScope.coroutineContext
    fun provideViewModelScope(viewModel: ViewModel) = viewModel.viewModelScope

}
