package com.bravedroid.jobby.companion

import com.bravedroid.jobby.companion.vm.FlowExt
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FormValidator @Inject constructor(
    private val logger: Logger,
    private val coroutineProvider: CoroutineProvider,

    ) {
    fun validateRegisterForm(
        nameSharedFlow: Flow<String>,
        emailSharedFlow: Flow<String>,
        passwordSharedFlow: Flow<String>
    ): Flow<Boolean> = FlowExt.combineOnThreeFlows(
        coroutineProvider.provideDispatcherCpu(),
        nameSharedFlow,
        emailSharedFlow,
        passwordSharedFlow
    ) { name, email, password ->
        logger.log(
            tag = "RegisterViewModel",
            msg = "name: $name, email: $email, password: $password",
            priority = Priority.V
        )
        validateForm(name, email, password)
    }

    fun validateLoginForm(
        emailSharedFlow: Flow<String>,
        passwordSharedFlow: Flow<String>
    ): Flow<Boolean> = FlowExt.combineOn(
        Dispatchers.Default,
        emailSharedFlow,
        passwordSharedFlow
    ) {  email, password ->
        logger.log(
            tag = "RegisterViewModel",
            msg = "email: $email, password: $password",
            priority = Priority.V
        )
        validateForm(email, password)
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        val isValidName = name.isNotBlank()
        val isValidEmail = email.isNotBlank() && email.contains("@")
        val isValidPassword = password.isNotBlank() && password.length >= 8
        return isValidName && isValidEmail && isValidPassword
    }

    private fun validateForm( email: String, password: String): Boolean {
        val isValidEmail = email.isNotBlank() && email.contains("@")
        val isValidPassword = password.isNotBlank() && password.length >= 8
        return isValidEmail && isValidPassword
    }
}
