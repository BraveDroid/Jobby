package com.bravedroid.jobby.companion.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bravedroid.jobby.companion.databinding.ActivityLoginBinding
import com.bravedroid.jobby.companion.vm.LoginViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

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
    }

    private fun navigateToUserProfile() {
        with(
            Intent(this@LoginActivity, UserProfileActivity::class.java)
        ) {
            startActivity(this)
        }
    }
}
