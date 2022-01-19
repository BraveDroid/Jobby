package com.bravedroid.jobby.domain.tracking

sealed class Event {
    object AppLaunchedEvent : Event()
    data class ScreenViewEvent(val screenName: String) : Event()
    data class ClickEvent(val targetName: String) : Event()

    sealed class LoginEvent : Event() {
        object SignUpEvent : LoginEvent()
        object SignInEvent : LoginEvent()
        object SignOutEvent : LoginEvent()
    }
}
