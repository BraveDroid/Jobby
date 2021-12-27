package com.bravedroid.jobby.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
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
    init {
        Log.d("LoginFragment", "LoginFragment ${hashCode()}")
    }

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var formValidator: FormValidator

    private var _bindingLogin: FragmentLoginBinding? = null
    private val bindingLogin get() = _bindingLogin!!

    private val viewModel: LoginViewModel by viewModels()
    private val emailSharedFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val passwordSharedFlow: MutableStateFlow<String> = MutableStateFlow("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _bindingLogin = FragmentLoginBinding.inflate(inflater, container, false)
        return bindingLogin.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (bindingLogin.emailTextInput.editText?.text?.isEmpty()!!
            && bindingLogin.passwordTextInput.editText?.text?.isEmpty()!!
        ) {
            bindingLogin.loginBtn.isEnabled = false
        }
        val bindingLogin = DataBindingUtil.bind<FragmentLoginBinding>(requireView())!!
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

                bindingLogin.emailTextInput.error = validation.emailErrorMessage
                bindingLogin.emailTextInput.setErrorIconDrawable(getErrorIconRes(validation.emailErrorMessage))

                bindingLogin.passwordTextInput.error = validation.passwordErrorMessage
                bindingLogin.passwordTextInput.setErrorIconDrawable(getErrorIconRes(validation.passwordErrorMessage))

                logger.log("LoginActivity", "$validation", Priority.V)
            }.launchIn(lifecycleScope)

        savedInstanceState?.putCharSequence(
            "emailTextInput",
            bindingLogin.emailTextInput.editText?.text?.trim()
        )
        savedInstanceState?.putCharSequence(
            "passwordTextInput",
            bindingLogin.emailTextInput.editText?.text?.trim()
        )
    }

    private fun getErrorIconRes(msg: String?): Int = if (msg == null)
        0
    else {
        R.drawable.ic_error_outline
    }

    private fun navigateToUserProfile() {
//        with(
//            Intent(this@LoginActivity, UserProfileActivity::class.java)
//        ) {
//            startActivity(this)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingLogin = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        bindingLogin.emailTextInput.editText?.setText(outState.get("emailTextInput"))
//        bindingLogin.passwordTextInput.editText?.setText( outState.get("passwordTextInput"))
    }
}
