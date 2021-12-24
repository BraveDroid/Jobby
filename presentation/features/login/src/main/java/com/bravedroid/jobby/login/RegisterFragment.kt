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
import com.bravedroid.jobby.login.databinding.FragmentRegisterBinding
import com.bravedroid.jobby.login.vm.RegisterViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
//        bindingRegister.goToLoginBtn.setOnClickListener {
//            navigateToLogin()
//        }
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
                    Snackbar.make(bindingRegister.root, "$it", BaseTransientBottomBar.LENGTH_SHORT).show()
                    navigateToLogin()
                }
                is RegisterViewModel.UiEvent.ShowError -> {
                    Snackbar.make(bindingRegister.root, "$it", BaseTransientBottomBar.LENGTH_SHORT).show()
                }
            }
            bindingRegister.registerBtn.isEnabled = true
        }.launchIn(lifecycleScope)


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
            .onEach { isValid ->
                logger.log("RegisterActivity", "$isValid", Priority.V)
                bindingRegister.registerBtn.isEnabled = isValid
            }.launchIn(lifecycleScope)
    }

    private fun navigateToLogin() {
//        with(
//            Intent(this@RegisterActivity, LoginActivity::class.java)
//        ) {
//            startActivity(this)
//        }
    }
}
