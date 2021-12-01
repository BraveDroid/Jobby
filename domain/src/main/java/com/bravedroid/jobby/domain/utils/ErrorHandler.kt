package com.bravedroid.jobby.domain.utils

import com.bravedroid.jobby.domain.entities.ErrorEntity

interface ErrorHandler {
    fun handle(t: Throwable): ErrorEntity
}
