package com.bravedroid.jobby.core.tracking

import com.bravedroid.jobby.domain.tracking.Event
import com.bravedroid.jobby.domain.tracking.EventTracker
import com.bravedroid.jobby.domain.tracking.JobbyTracker
import javax.inject.Inject

class EventsTracker @Inject constructor(
    @JobbyTracker private val eventsTracker: EventTracker,
) {
    fun trackLoginScreenViewEvent() {
        eventsTracker.track(Event.ScreenViewEvent("Login"))
    }

    fun trackRegisterScreenViewEvent() {
        eventsTracker.track(Event.ScreenViewEvent("Register"))
    }

    fun trackHomeScreenViewEvent() {
        eventsTracker.track(Event.ScreenViewEvent("Home"))
    }

    fun trackAppLaunchedEvent() {
        eventsTracker.track(Event.AppLaunchedEvent)
    }

    fun trackLoginClickEvent() {
        eventsTracker.track(Event.ClickEvent("login"))
    }

    fun trackRegisterClickEvent() {
        eventsTracker.track(Event.ClickEvent("register"))
    }

    fun trackUserLoggedInEvent() {
        eventsTracker.track(Event.LoginEvent.SignInEvent)
    }

    fun trackUserSignedUpEvent() {
        eventsTracker.track(Event.LoginEvent.SignUpEvent)
    }

    fun trackUserLoggedOutEvent() {
        eventsTracker.track(Event.LoginEvent.SignOutEvent)
    }
}
