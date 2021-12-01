package com.bravedroid.jobby.domain.log

interface Logger {
    fun log(tag: String, msg: String, priority: Priority = Priority.D)
    fun log(tag: String, t: Throwable)
    fun init()
}


