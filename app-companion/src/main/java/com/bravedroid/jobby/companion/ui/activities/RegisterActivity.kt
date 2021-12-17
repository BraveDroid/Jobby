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
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    @Inject
    internal lateinit var logger: Logger
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding

    private val nameStateFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val emailStateFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val passwordStateFlow: MutableStateFlow<String> = MutableStateFlow("")

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
            if (text != null) nameStateFlow.value = text.toString()
        }
        binding.editTextEmail.doOnTextChanged { text, _, _, _ ->
            if (text != null) emailStateFlow.value = text.toString()
        }
        binding.editTextPassword.doOnTextChanged { text, _, _, _ ->
            if (text != null) passwordStateFlow.value = text.toString()
        }

        viewModel.validateRegisterForm(nameStateFlow, emailStateFlow, passwordStateFlow)
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
