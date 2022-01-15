package com.bravedroid.jobby.login.register

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
import com.bravedroid.jobby.login.databinding.FragmentRegisterBinding
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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
        setListeners()
        viewModel.registerUiModelStateFlow.onEach {
            bindingRegister.editTextUserName.editText?.setText(it.name)
            bindingRegister.editTextEmail.editText?.setText(it.email)
            bindingRegister.editTextPassword.editText?.setText(it.password)
            bindingRegister.registerBtn.isEnabled = it.isValid

            nameStateFlow.value = bindingRegister.editTextUserName.editText?.text.toString()
            emailStateFlow.value = bindingRegister.editTextEmail.editText?.text.toString()
            passwordStateFlow.value = bindingRegister.editTextPassword.editText?.text.toString()

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.uiEventFlow.onEach {
            when (it) {
                RegisterViewModel.UiEvent.NavigationToLoginScreen -> {
                    showSnackbar(message = "$it", color = R.attr.colorSecondary)
                    navigateToLogin()
                }
                is RegisterViewModel.UiEvent.ShowError -> {
                    showSnackbar(message = it.errorMessage, color = R.attr.colorError)
                }
            }
            bindingRegister.registerBtn.isEnabled = true
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.validateRegisterForm(nameStateFlow, emailStateFlow, passwordStateFlow)
            .debounce(500)
            .drop(1)
            .flowOn(Dispatchers.Default)
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
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    @SuppressLint("ShowToast")
    private fun showSnackbar(message: String, @AttrRes color: Int) = with(
        Snackbar.make(bindingRegister.root, message, BaseTransientBottomBar.LENGTH_SHORT)
            .setBackgroundTint(MaterialColors.getColor(bindingRegister.root, color))
    ) {
        show()
    }

    private fun setListeners() {
        bindingRegister.editTextUserName.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null) nameStateFlow.value = text.toString()
        }
        bindingRegister.editTextEmail.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null) emailStateFlow.value = text.toString()
        }
        bindingRegister.editTextPassword.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null) passwordStateFlow.value = text.toString()
        }
        bindingRegister.loginLinkTextView.setOnClickListener {
            Log.d("RegisterFragment", "loginLinkTextView clicked")
            it.findNavController().navigate(R.id.loginFragment)
        }
        bindingRegister.registerBtn.setOnClickListener {
            it.isEnabled = false
            viewModel.register(
                RegisterUiModel(
                    email = bindingRegister.editTextEmail.editText?.text.toString(),
                    name = bindingRegister.editTextUserName.editText?.text.toString(),
                    password = bindingRegister.editTextPassword.editText?.text.toString(),
                )
            )
        }
    }

    private fun getErrorIconRes(msg: String?): Int =
        if (msg == null) 0 else R.drawable.ic_error_outline

    private fun navigateToLogin() {
        // TODO:RF 15/01/2022 deeplink
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

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingRegister = null
    }
}
