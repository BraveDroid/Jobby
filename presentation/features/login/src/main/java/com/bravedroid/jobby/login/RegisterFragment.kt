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
import com.bravedroid.jobby.login.databinding.FragmentRegisterBinding
import com.bravedroid.jobby.login.vm.LoginViewModel
import com.bravedroid.jobby.login.vm.RegisterViewModel
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    @Inject
    lateinit var logger: Logger

    private var _bindingRegister: FragmentRegisterBinding? = null
    private val bindingRegister get() = _bindingRegister!!

    private val nameStateFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val emailStateFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val passwordStateFlow: MutableStateFlow<String> = MutableStateFlow("")

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingRegister = FragmentRegisterBinding.inflate(inflater, container, false)
        return bindingRegister.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.registerUiModelStateFlow.onEach {
            bindingRegister.editTextUserName.editText?.setText(it.name)
            bindingRegister.editTextEmail.editText?.setText(it.email)
            bindingRegister.editTextPassword.editText?.setText(it.password)
            bindingRegister.registerBtn.isEnabled = it.isValid
        }.launchIn(lifecycleScope)

        if (bindingRegister.editTextUserName.editText?.text?.isEmpty()!!
            && bindingRegister.editTextEmail.editText?.text?.isEmpty()!!
            && bindingRegister.editTextPassword.editText?.text?.isEmpty()!!
        ) {
            bindingRegister.registerBtn.isEnabled = false
        }
        bindingRegister.loginLinkTextView.setOnClickListener {
            Log.d("RegisterFragment", "loginLinkTextView clicked")
            it.findNavController().navigate(R.id.loginFragment)
        }
        bindingRegister.registerBtn.setOnClickListener {
            it.isEnabled = false
            viewModel.register(
                RegisterViewModel.RegisterUiModel(
                    email = bindingRegister.editTextEmail.editText?.text.toString(),
                    name = bindingRegister.editTextUserName.editText?.text.toString(),
                    password = bindingRegister.editTextPassword.editText?.text.toString(),
                )
            )
        }

        viewModel.uiEventFlow.onEach {
            when (it) {
                RegisterViewModel.UiEvent.NavigationToLoginScreen -> {
                    val snackbar = Snackbar.make(
                        bindingRegister.root,
                        "$it",
                        BaseTransientBottomBar.LENGTH_SHORT
                    )
                        .setBackgroundTint(MaterialColors.getColor(view, R.attr.colorSecondary))
                    snackbar.show()
                    navigateToLogin()
                }
                is RegisterViewModel.UiEvent.ShowError -> {
                    val snackbar = Snackbar.make(
                        bindingRegister.root,
                        "$it",
                        BaseTransientBottomBar.LENGTH_SHORT
                    )
                        .setBackgroundTint(MaterialColors.getColor(view, R.attr.colorError))
                    snackbar.show()
                }
            }
            bindingRegister.registerBtn.isEnabled = true
        }.launchIn(lifecycleScope)

        nameStateFlow.value = bindingRegister.editTextUserName.editText?.text.toString()
        emailStateFlow.value = bindingRegister.editTextEmail.editText?.text.toString()
        passwordStateFlow.value = bindingRegister.editTextPassword.editText?.text.toString()

        bindingRegister.editTextUserName.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null) nameStateFlow.value = text.toString()
        }
        bindingRegister.editTextEmail.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null) emailStateFlow.value = text.toString()
        }
        bindingRegister.editTextPassword.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null) passwordStateFlow.value = text.toString()
        }

        viewModel.validateRegisterForm(nameStateFlow, emailStateFlow, passwordStateFlow)
            .debounce(500)
            .drop(1)
            .onEach { registerValidation ->
                logger.log("RegisterActivity", "${registerValidation.isValid}", Priority.V)
                bindingRegister.registerBtn.isEnabled = registerValidation.isValid

                if (registerValidation.nameErrorMessage != null) {
                    bindingRegister.editTextUserName.error = registerValidation.nameErrorMessage
                    bindingRegister.editTextUserName.setErrorIconDrawable(
                        getErrorIconRes(
                            registerValidation.nameErrorMessage
                        )
                    )
                    bindingRegister.registerBtn.isEnabled = false
                } else {
                    bindingRegister.editTextUserName.error = null
                    bindingRegister.editTextUserName.errorIconDrawable = null
                }


                if (registerValidation.emailErrorMessage != null) {
                    bindingRegister.editTextEmail.error = registerValidation.emailErrorMessage
                    bindingRegister.editTextEmail.setErrorIconDrawable(
                        getErrorIconRes(
                            registerValidation.emailErrorMessage
                        )
                    )
                    bindingRegister.registerBtn.isEnabled = false
                } else {
                    bindingRegister.editTextEmail.error = null
                    bindingRegister.editTextEmail.errorIconDrawable = null
                }


                if (registerValidation.passwordErrorMessage != null) {
                    bindingRegister.editTextPassword.error = registerValidation.passwordErrorMessage
                    bindingRegister.editTextPassword.setErrorIconDrawable(
                        getErrorIconRes(
                            registerValidation.passwordErrorMessage
                        )
                    )
                } else {
                    bindingRegister.editTextPassword.error = null
                    bindingRegister.editTextPassword.errorIconDrawable = null
                }
            }.launchIn(lifecycleScope)
    }

    private fun getErrorIconRes(msg: String?): Int = if (msg == null) 0
    else {
        R.drawable.ic_error_outline
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveRegisterState(
            RegisterViewModel.RegisterUiState(
                email = bindingRegister.editTextEmail.editText?.text?.toString()
                    ?: emailStateFlow.value,
                name = bindingRegister.editTextUserName.editText?.text?.toString()
                    ?: nameStateFlow.value,
                password = bindingRegister.editTextPassword.editText?.text?.toString()
                    ?: passwordStateFlow.value,
                isValid = bindingRegister.registerBtn.isEnabled
            )
        )
    }

    private fun navigateToLogin() {
    }
}
