package com.bravedroid.jobby.companion.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.bravedroid.jobby.companion.databinding.ActivityLoginBinding
import com.bravedroid.jobby.companion.vm.LoginViewModel
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    @Inject
    internal lateinit var logger: Logger

    private val emailSharedFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val passwordSharedFlow: MutableStateFlow<String> = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginBtn.setOnClickListener {
            it.isEnabled = false
            viewModel.login(
                LoginViewModel.LoginUiModel(
                    email = binding.emailEditText.text.toString(),
                    password = binding.passwordEditText.text.toString(),
                )
            )
        }

        viewModel.uiEventFlow.onEach {
            when (it) {
                LoginViewModel.UiEvent.NavigationToUserProfile -> {
                    Snackbar.make(binding.root, "$it", BaseTransientBottomBar.LENGTH_SHORT).show()
                    navigateToUserProfile()
                }
                is LoginViewModel.UiEvent.ShowError -> {
                    Snackbar.make(binding.root, "$it", BaseTransientBottomBar.LENGTH_SHORT).show()
                }
            }
            binding.loginBtn.isEnabled = true
        }.launchIn(lifecycleScope)

        binding.emailEditText.doOnTextChanged { text, _, _, _ ->
            if (text != null) emailSharedFlow.value = text.toString()
        }
        binding.passwordEditText.doOnTextChanged { text, _, _, _ ->
            if (text != null) passwordSharedFlow.value = text.toString()
        }

        viewModel.validateLoginForm(emailSharedFlow, passwordSharedFlow)
            .onEach { isValid ->
                logger.log("LoginActivity", "$isValid", Priority.V)
                binding.loginBtn.isEnabled = isValid
            }.launchIn(lifecycleScope)
    }

    private fun navigateToUserProfile() {
        with(
            Intent(this@LoginActivity, UserProfileActivity::class.java)
        ) {
            startActivity(this)
        }
    }
}
