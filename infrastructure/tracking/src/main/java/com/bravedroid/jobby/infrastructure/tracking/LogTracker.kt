package com.bravedroid.jobby.infrastructure.tracking

import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import com.bravedroid.jobby.domain.tracking.Event
import com.bravedroid.jobby.domain.tracking.EventTracker
import javax.inject.Inject

class LogTracker @Inject constructor(
    private val logger: Logger,
) : EventTracker {
    override fun track(event: Event) {
        logger.log(tag = "LogTracker", msg = "track event : $event", priority = Priority.V)
    }
}

