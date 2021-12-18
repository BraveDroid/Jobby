package com.bravedroid.jobby.login
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CoroutineProvider @Inject constructor() {
    fun provideViewModelContext(viewModel: ViewModel) = viewModel.viewModelScope.coroutineContext
    fun provideViewModelScope(viewModel: ViewModel) = viewModel.viewModelScope

    fun provideDispatcherCpu() =  Dispatchers.Default
    fun provideDispatcherIO() =  Dispatchers.IO
    fun provideDispatcherMain() =  Dispatchers.Main
    fun provideDispatcherUnconfined() =  Dispatchers.Unconfined
}
