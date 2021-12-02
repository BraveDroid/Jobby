package com.bravedroid.jobby.companion.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bravedroid.jobby.companion.R
import com.bravedroid.jobby.companion.databinding.ActivityLoginBinding
import com.bravedroid.jobby.companion.databinding.ActivityUserProfileBinding
import com.bravedroid.jobby.companion.vm.RegisterViewModel
import com.bravedroid.jobby.companion.vm.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {
    private val viewModel: UserProfileViewModel by viewModels()

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.textView.text=""
        binding.textView2.text=""

    }
}
