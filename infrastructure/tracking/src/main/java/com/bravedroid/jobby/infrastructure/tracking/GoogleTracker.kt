package com.bravedroid.jobby.infrastructure.tracking

import com.bravedroid.jobby.domain.tracking.Event
import com.bravedroid.jobby.domain.tracking.EventTracker
import com.bravedroid.jobby.infrastructure.tracking.di.GoogleEventTracker
import javax.inject.Inject

class GoogleTracker @Inject constructor(
) : EventTracker {
    override fun track(event: Event) {
        //to be done
    }
}
