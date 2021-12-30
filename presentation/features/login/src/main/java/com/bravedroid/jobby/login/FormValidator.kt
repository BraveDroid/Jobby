package com.bravedroid.jobby.login

import androidx.annotation.VisibleForTesting
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import com.bravedroid.jobby.login.vm.FlowExt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.util.regex.Pattern.compile
import javax.inject.Inject

class FormValidator @Inject constructor(
    private val logger: Logger,
    private val coroutineProvider: CoroutineProvider,
) {
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
        validateForm(name, email, password)
    }

    fun validateLoginForm(
        emailSharedFlow: Flow<String>,
        passwordSharedFlow: Flow<String>
    ): Flow<Validation> = FlowExt.combineOn(
        Dispatchers.Default,
        emailSharedFlow,
        passwordSharedFlow
    ) { email, password ->
        logger.log(
            tag = "RegisterViewModel",
            msg = "email: $email, password: $password",
            priority = Priority.V
        )
        validateForm(email, password)
    }

    private fun validateForm(name: String, email: String, password: String): RegisterValidation {
        val isValidName = name.isNotBlank()
        val isValidEmail = email.isNotBlank() && isEmail(email)
        val isValidPassword = password.isNotBlank() && password.length >= 8

        val validation: RegisterValidation = when {
            isValidName && isValidEmail && isValidPassword -> {
                RegisterValidation(true, null, null,null)
            }
            !isValidName && isValidEmail && isValidPassword -> {
                RegisterValidation(false, "Invalid Name", null,null)
            }
            !isValidName && !isValidEmail && isValidPassword -> {
                RegisterValidation(false, "Invalid Name", "Invalid Email",null)
            }
            !isValidName && isValidEmail && !isValidPassword -> {
                RegisterValidation(false, "Invalid Name", null,"Invalid Password")
            }
            isValidName && !isValidEmail && isValidPassword -> {
                RegisterValidation(false, null, "Invalid Email",null)
            }
            isValidName && !isValidEmail && !isValidPassword -> {
                RegisterValidation(false, null, "Invalid Email","Invalid Password")
            }
            isValidName && isValidEmail && !isValidPassword -> {
                RegisterValidation(false, null, null,"Invalid Password")
            }else -> {
                RegisterValidation(false, "Invalid Name", "Invalid Email","Invalid Password")
            }
        }
        return validation
    }

    private fun validateForm(email: String, password: String): Validation {
        val isValidEmail = email.isNotBlank() && isEmail(email)
        val isValidPassword = password.isNotBlank() && password.length >= 8

        val validation: Validation = when {
            isValidEmail && isValidPassword -> {
                Validation(true, null, null)
            }
            isValidEmail && !isValidPassword -> {
                Validation(false, null, "Invalid Password")
            }
            !isValidEmail && isValidPassword -> {
                Validation(false, "Invalid Email", null)
            }
            else -> {
                Validation(false, "Invalid Email", "Invalid Password")
            }
        }

        return validation
    }

    private val emailRegex = compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun isEmail(email: String): Boolean {
        return emailRegex.matcher(email).matches()
    }
}

data class Validation(
    val isValid: Boolean,
    val emailErrorMessage: String?,
    val passwordErrorMessage: String?,
)

data class RegisterValidation(
    val isValid: Boolean,
    val nameErrorMessage: String?,
    val emailErrorMessage: String?,
    val passwordErrorMessage: String?,
)
