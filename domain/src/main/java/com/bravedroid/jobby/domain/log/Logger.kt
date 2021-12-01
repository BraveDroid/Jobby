package com.bravedroid.jobby.domain.log

interface Logger {
    fun log(msg: String, tag: String, priority: Priority)
    fun init()
}


