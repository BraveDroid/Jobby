package com.bravedroid.jobby.presentation.util

sealed class PageState<out R> {
    object Loading : PageState<Nothing>()
    object Error : PageState<Nothing>()
    data class Content<out T>(val data: T) : PageState<T>()
}
