package com.bravedroid.jobby.login.util

import androidx.annotation.VisibleForTesting
import com.bravedroid.jobby.core.CoroutineProvider
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import com.bravedroid.jobby.domain.utils.FlowExt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.util.regex.Pattern.compile
import javax.inject.Inject

class FormValidator @Inject constructor(
    private val logger: Logger,
    private val coroutineProvider: CoroutineProvider,
) {
    private companion object {
        private const val EMAIL_REGEX = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    }

    fun validateLoginForm(
        emailSharedFlow: Flow<String>,
        passwordSharedFlow: Flow<String>
    ): Flow<LoginValidation> = FlowExt.combineOn(
        Dispatchers.Default,
        emailSharedFlow,
        passwordSharedFlow
    ) { email, password ->
        logger.log(
            tag = "RegisterViewModel",
            msg = "email: $email, password: $password",
            priority = Priority.V,
        )
        validateLogin(email, password)
    }

    private fun validateLogin(email: String, password: String): LoginValidation {
        val isValidEmail = email.isNotBlank() && isEmail(email)
        val isValidPassword = password.isNotBlank() && password.length >= 8

        val loginValidation: LoginValidation = when {
            isValidEmail && isValidPassword -> {
                LoginValidation(true, null, null)
            }
            isValidEmail && !isValidPassword -> {
                LoginValidation(false, null, "Invalid Password")
            }
            !isValidEmail && isValidPassword -> {
                LoginValidation(false, "Invalid Email", null)
            }
            else -> {
                LoginValidation(false, "Invalid Email", "Invalid Password")
            }
        }

        return loginValidation
    }

    fun validateRegisterForm(
        nameSharedFlow: Flow<String>,
        emailSharedFlow: Flow<String>,
        passwordSharedFlow: Flow<String>
    ): Flow<RegisterValidation> = FlowExt.combineOnThreeFlows(
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
        validateRegister(name, email, password)
    }

    private fun validateRegister(
        name: String,
        email: String,
        password: String
    ): RegisterValidation {
        val isValidName = name.isNotBlank() && name.length > 1
        val isValidEmail = email.isNotBlank() && isEmail(email)
        val isValidPassword = password.isNotBlank() && password.length >= 8

// TODO:RF 14/01/2022 refactor to enum map
        val validation: RegisterValidation = when {
            isValidName && isValidEmail && isValidPassword -> {
                RegisterValidation(true, null, null, null)
            }
            !isValidName && isValidEmail && isValidPassword -> {
                RegisterValidation(false, "Invalid Name", null, null)
            }
            !isValidName && !isValidEmail && isValidPassword -> {
                RegisterValidation(false, "Invalid Name", "Invalid Email", null)
            }
            !isValidName && isValidEmail && !isValidPassword -> {
                RegisterValidation(false, "Invalid Name", null, "Invalid Password")
            }
            isValidName && !isValidEmail && isValidPassword -> {
                RegisterValidation(false, null, "Invalid Email", null)
            }
            isValidName && !isValidEmail && !isValidPassword -> {
                RegisterValidation(false, null, "Invalid Email", "Invalid Password")
            }
            isValidName && isValidEmail && !isValidPassword -> {
                RegisterValidation(false, null, null, "Invalid Password")
            }
            else -> {
                RegisterValidation(false, "Invalid Name", "Invalid Email", "Invalid Password")
            }
        }
        return validation
    }


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun isEmail(email: String): Boolean = compile(
        EMAIL_REGEX
    ).matcher(email).matches()
}
