package com.bravedroid.jobby.companion.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bravedroid.jobby.companion.databinding.ActivityRegisterBinding
import com.bravedroid.jobby.companion.vm.RegisterViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding

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

    }

    private fun navigateToLogin() {
        with(
            Intent(this@RegisterActivity, LoginActivity::class.java)
        ) {
            startActivity(this)
        }
    }
}

