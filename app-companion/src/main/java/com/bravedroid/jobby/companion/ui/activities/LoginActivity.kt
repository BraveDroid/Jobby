package com.bravedroid.jobby.companion.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bravedroid.jobby.companion.R
import com.bravedroid.jobby.companion.databinding.ActivityLoginBinding
import com.bravedroid.jobby.companion.databinding.ActivityRegisterBinding
import com.bravedroid.jobby.companion.vm.LoginViewModel
import com.bravedroid.jobby.companion.vm.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.button3.setOnClickListener {

        }

        binding.editTextTextEmailAddress2.text

        binding.editTextTextPassword2.text
    }
}
