package com.bravedroid.jobby.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import com.bravedroid.jobby.login.databinding.FragmentLoginBinding
import com.bravedroid.jobby.login.vm.LoginViewModel
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var formValidator: FormValidator

    private var _bindingLogin: FragmentLoginBinding? = null
    private val bindingLogin get() = _bindingLogin!!

    private val emailSharedFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val passwordSharedFlow: MutableStateFlow<String> = MutableStateFlow("")

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _bindingLogin = FragmentLoginBinding.inflate(inflater, container, false)
        return bindingLogin.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loginUiModelStateFlow.onEach {
            bindingLogin.emailTextInput.editText?.setText(it.email)
            bindingLogin.passwordTextInput.editText?.setText(it.password)
            bindingLogin.loginBtn.isEnabled = it.isValid
        }.launchIn(lifecycleScope)

        if (bindingLogin.emailTextInput.editText?.text?.isEmpty()!!
            && bindingLogin.passwordTextInput.editText?.text?.isEmpty()!!
        ) {
            bindingLogin.loginBtn.isEnabled = false
        }
        bindingLogin.registerLinkTextView.setOnClickListener {
            Log.d("LoginFragment", "registerLinkTextView clicked")
            it.findNavController().navigate(R.id.registerFragment)
        }
        bindingLogin.loginBtn.setOnClickListener {
            it.isEnabled = false
            viewModel.login(
                LoginViewModel.LoginUiModel(
                    email = bindingLogin.emailTextInput.editText?.text.toString(),
                    password = bindingLogin.passwordTextInput.editText?.text.toString(),
                )
            )
        }

        viewModel.uiEventFlow.onEach {
            when (it) {
                LoginViewModel.UiEvent.NavigationToUserProfile -> {
                    val snackbar =
                        Snackbar.make(bindingLogin.root, "$it", BaseTransientBottomBar.LENGTH_SHORT)
                            .setBackgroundTint(MaterialColors.getColor(view, R.attr.colorSecondary))
                    snackbar.show()
                    navigateToUserProfile()
                }
                is LoginViewModel.UiEvent.ShowError -> {
                    val snackbar =
                        Snackbar.make(bindingLogin.root, "$it", BaseTransientBottomBar.LENGTH_SHORT)
                            .setBackgroundTint(MaterialColors.getColor(view, R.attr.colorError))
                    snackbar.show()
                }
            }
            bindingLogin.loginBtn.isEnabled = true
        }.launchIn(lifecycleScope)

        emailSharedFlow.value = bindingLogin.emailTextInput.editText?.text.toString()
        passwordSharedFlow.value = bindingLogin.passwordTextInput.editText?.text.toString()

        bindingLogin.emailTextInput.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null) emailSharedFlow.value = text.toString()
        }

        bindingLogin.passwordTextInput.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null) passwordSharedFlow.value = text.toString()
        }

        formValidator.validateLoginForm(emailSharedFlow, passwordSharedFlow)
            .debounce(500)
            .drop(1)
            .onEach { validation ->
                bindingLogin.loginBtn.isEnabled = validation.isValid

                if (validation.emailErrorMessage != null) {
                    bindingLogin.emailTextInput.error = validation.emailErrorMessage
                    bindingLogin.emailTextInput.setErrorIconDrawable(getErrorIconRes(validation.emailErrorMessage))
                    bindingLogin.loginBtn.isEnabled = false
                } else {
                    bindingLogin.emailTextInput.error = null
                    bindingLogin.emailTextInput.errorIconDrawable = null
                }

                if (validation.passwordErrorMessage != null) {
                    bindingLogin.passwordTextInput.error = validation.passwordErrorMessage
                    bindingLogin.passwordTextInput.setErrorIconDrawable(getErrorIconRes(validation.passwordErrorMessage))
                    bindingLogin.loginBtn.isEnabled = false
                } else {
                    bindingLogin.passwordTextInput.error = null
                    bindingLogin.passwordTextInput.errorIconDrawable = null
                }

                logger.log("LoginActivity", "$validation", Priority.V)
            }.launchIn(lifecycleScope)
    }

    private fun getErrorIconRes(msg: String?): Int = if (msg == null) 0
    else {
        R.drawable.ic_error_outline
    }

    private fun navigateToUserProfile() {
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveLoginState(
            LoginViewModel.LoginUiState(
                email = bindingLogin.emailTextInput.editText?.text?.toString()
                    ?: emailSharedFlow.value,
                password = bindingLogin.passwordTextInput.editText?.text?.toString()
                    ?: passwordSharedFlow.value,
                isValid = bindingLogin.loginBtn.isEnabled
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingLogin = null
    }
}
