package com.bravedroid.jobby.companion.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.bravedroid.jobby.companion.databinding.ActivityRegisterBinding
import com.bravedroid.jobby.companion.vm.RegisterViewModel
import com.bravedroid.jobby.domain.log.Logger
import com.bravedroid.jobby.domain.log.Priority
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    @Inject
    internal lateinit var logger: Logger
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding

    private val nameSharedFlow: MutableSharedFlow<String> = MutableSharedFlow()
    private val emailSharedFlow: MutableSharedFlow<String> = MutableSharedFlow()
    private val passwordSharedFlow: MutableSharedFlow<String> = MutableSharedFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.goToLoginBtn.setOnClickListener {
            navigateToLogin()
        }
        binding.registerBtn.setOnClickListener {
            it.isEnabled = false
            viewModel.register(
                RegisterViewModel.RegisterUiModel(
                    email = binding.editTextEmail.text.toString(),
                    name = binding.editTextUserName.text.toString(),
                    password = binding.editTextPassword.text.toString(),
                )
            )
        }

        viewModel.uiEventFlow.onEach {
            when (it) {
                RegisterViewModel.UiEvent.NavigationToLoginScreen -> {
                    Snackbar.make(binding.root, "$it", LENGTH_SHORT).show()
                    navigateToLogin()
                }
                is RegisterViewModel.UiEvent.ShowError -> {
                    Snackbar.make(binding.root, "$it", LENGTH_SHORT).show()
                }
            }
            binding.registerBtn.isEnabled = true
        }.launchIn(lifecycleScope)


        binding.editTextUserName.doOnTextChanged { text, _, _, _ ->
            lifecycleScope.launch {
                if (text != null) nameSharedFlow.emit(text.toString())
            }
        }
        binding.editTextEmail.doOnTextChanged { text, _, _, _ ->
            lifecycleScope.launch {
                if (text != null) emailSharedFlow.emit(text.toString())
            }
        }
        binding.editTextPassword.doOnTextChanged { text, _, _, _ ->
            lifecycleScope.launch {
                if (text != null) passwordSharedFlow.emit(text.toString())
            }
        }

        viewModel.validateRegisterForm(nameSharedFlow, emailSharedFlow, passwordSharedFlow)
            .onEach { isValid ->
                logger.log("RegisterActivity", "$isValid", Priority.V)
                binding.registerBtn.isEnabled = isValid
            }.launchIn(lifecycleScope)
    }

    private fun navigateToLogin() {
        with(
            Intent(this@RegisterActivity, LoginActivity::class.java)
        ) {
            startActivity(this)
        }
    }
}
