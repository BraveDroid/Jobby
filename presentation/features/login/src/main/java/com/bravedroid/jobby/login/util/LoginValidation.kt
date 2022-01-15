package com.bravedroid.jobby.login.util

data class LoginValidation(
    val isValid: Boolean,
    val emailErrorMessage: String?,
    val passwordErrorMessage: String?,
)

