package com.bravedroid.jobby.login.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import com.bravedroid.jobby.login.R
import com.bravedroid.jobby.login.databinding.FragmentLoginBinding
import com.bravedroid.jobby.login.util.FormValidator
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var formValidator: FormValidator

    private var _bindingLogin: FragmentLoginBinding? = null
    private val bindingLogin get() = _bindingLogin!!

    private val emailStateFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val passwordStateFlow: MutableStateFlow<String> = MutableStateFlow("")

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
        setListeners()
        // TODO:RF 15/01/2022 fix leak launchIn
        viewModel.loginUiModelStateFlow.onEach {
            bindingLogin.emailTextInput.editText?.setText(it.email)
            bindingLogin.passwordTextInput.editText?.setText(it.password)
            bindingLogin.loginBtn.isEnabled = it.isValid

            emailStateFlow.value = bindingLogin.emailTextInput.editText?.text.toString()
            passwordStateFlow.value = bindingLogin.passwordTextInput.editText?.text.toString()

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        // TODO:RF 15/01/2022 fix leak launchIn
        viewModel.uiEventFlow.onEach {
            when (it) {
                LoginViewModel.UiEvent.NavigationToUserProfile -> {
                    showSnackbar("$it", R.attr.colorSecondary)
                    navigateToHomeScreen()
                }
                is LoginViewModel.UiEvent.ShowError -> {
                    showSnackbar(message = it.errorMessage, color = R.attr.colorError)
                }
            }
            bindingLogin.loginBtn.isEnabled = true
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        // TODO:RF 15/01/2022 fix leak launchIn
        formValidator.validateLoginForm(emailStateFlow, passwordStateFlow)
            .debounce(500)
            .drop(1)
            .flowOn(Dispatchers.Default)
            .onEach { validation ->
                logger.log("LoginActivity", "$validation", Priority.V)

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
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    @SuppressLint("ShowToast")
    private fun showSnackbar(message: String, @AttrRes color: Int) = with(
        Snackbar.make(bindingLogin.root, message, BaseTransientBottomBar.LENGTH_SHORT)
            .setBackgroundTint(MaterialColors.getColor(bindingLogin.root, color))
    ) {
        show()
    }

    private fun setListeners() {
        bindingLogin.emailTextInput.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null) emailStateFlow.value = text.toString()
        }
        bindingLogin.passwordTextInput.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null) passwordStateFlow.value = text.toString()
        }
        bindingLogin.registerLinkTextView.setOnClickListener {
            Log.d("LoginFragment", "registerLinkTextView clicked")
            // TODO: 15/01/2022 navigation with directions
            it.findNavController().navigate(R.id.registerFragment)
        }
        bindingLogin.loginBtn.setOnClickListener {
            it.isEnabled = false
            viewModel.login(
                LoginUiModel(
                    email = bindingLogin.emailTextInput.editText?.text.toString(),
                    password = bindingLogin.passwordTextInput.editText?.text.toString(),
                )
            )
        }
    }

    private fun getErrorIconRes(msg: String?): Int =
        if (msg == null) 0 else R.drawable.ic_error_outline

    private fun navigateToHomeScreen() {
        // TODO:RF 15/01/2022 deeplink
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveLoginState(
            LoginViewModel.LoginUiState(
                email = bindingLogin.emailTextInput.editText?.text?.toString()
                    ?: emailStateFlow.value,
                password = bindingLogin.passwordTextInput.editText?.text?.toString()
                    ?: passwordStateFlow.value,
                isValid = bindingLogin.loginBtn.isEnabled,
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingLogin = null
    }
}
