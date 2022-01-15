package com.bravedroid.jobby.login.util

data class RegisterValidation(
    val isValid: Boolean,
    val nameErrorMessage: String?,
    val emailErrorMessage: String?,
    val passwordErrorMessage: String?,
)
