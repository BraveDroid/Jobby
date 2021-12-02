package com.bravedroid.jobby.companion.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bravedroid.jobby.companion.PageState
import com.bravedroid.jobby.companion.databinding.ActivityRegisterBinding
import com.bravedroid.jobby.companion.vm.RegisterViewModel
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

        binding.registerBtn.setOnClickListener {
            viewModel.register(
                RegisterViewModel.RegisterUiModel(
                    email = binding.editTextEmail.text.toString(),
                    name = binding.editTextUserName.text.toString(),
                    password = binding.editTextPassword.text.toString(),
                )
            )
        }

        viewModel.pageStateFlow.onEach {
            when (it) {
                is PageState.Content -> {
                    TODO("")
                }
                PageState.Error -> {
                    TODO()
                }
                PageState.Loading -> {
                    TODO()
                }
            }
        }.launchIn(lifecycleScope)

    }
}

