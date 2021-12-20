package com.bravedroid.jobby.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import com.bravedroid.jobby.login.databinding.FragmentLoginBinding
import com.bravedroid.jobby.login.vm.LoginViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var logger: Logger

    private var _bindingLogin: FragmentLoginBinding? = null
    private val bindingLogin get() = _bindingLogin!!

    private val viewModel: LoginViewModel by viewModels()
    private val emailSharedFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val passwordSharedFlow: MutableStateFlow<String> = MutableStateFlow("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingLogin = FragmentLoginBinding.inflate(inflater, container, false)
        return bindingLogin.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
                    Snackbar.make(bindingLogin.root, "$it", BaseTransientBottomBar.LENGTH_SHORT)
                        .show()
                    navigateToUserProfile()
                }
                is LoginViewModel.UiEvent.ShowError -> {
                    Snackbar.make(bindingLogin.root, "$it", BaseTransientBottomBar.LENGTH_SHORT)
                        .show()
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

        viewModel.validateLoginForm(emailSharedFlow, passwordSharedFlow)
            .onEach { isValid ->
                logger.log("LoginActivity", "$isValid", Priority.V)
                bindingLogin.loginBtn.isEnabled = isValid
            }.launchIn(lifecycleScope)

        bindingLogin.registerLinkTextView.setOnClickListener {

        }
    }

    private fun navigateToUserProfile() {
//        with(
//            Intent(this@LoginActivity, UserProfileActivity::class.java)
//        ) {
//            startActivity(this)
//        }
    }
}
