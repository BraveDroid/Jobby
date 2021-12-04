package com.bravedroid.jobby.companion.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bravedroid.jobby.companion.databinding.ActivityUserProfileBinding
import com.bravedroid.jobby.companion.vm.UserProfileViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {
    private val viewModel: UserProfileViewModel by viewModels()

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.uiEventFlow.onEach {
            when (it) {
                is UserProfileViewModel.UiEvent.ShowError -> {
                    Snackbar.make(binding.root, "$it", LENGTH_SHORT).show()
                }
                is UserProfileViewModel.UiEvent.UserProfileUiModel -> {
                    binding.userNameValueTextView.text = it.name
                    binding.emailValueTextView.text = it.email
                }
            }
        }.launchIn(lifecycleScope)

        viewModel.findUser()

        binding.refreshBtn.setOnClickListener {
            viewModel.findUser()
        }
    }
}
