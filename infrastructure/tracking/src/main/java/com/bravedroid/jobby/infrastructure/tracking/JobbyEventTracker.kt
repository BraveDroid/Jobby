package com.bravedroid.jobby.infrastructure.tracking

import com.bravedroid.jobby.domain.tracking.Event
import com.bravedroid.jobby.domain.tracking.EventTracker
import javax.inject.Inject

class JobbyEventTracker @Inject constructor(
    private val trackers: List<EventTracker>,
) : EventTracker {
    override fun track(event: Event) {
        for (tracker in trackers) {
            tracker.track(event)
        }
    }
}
