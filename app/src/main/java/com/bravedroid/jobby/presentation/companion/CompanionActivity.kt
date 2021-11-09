package com.bravedroid.jobby.presentation.companion

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bravedroid.jobby.databinding.ActivityCompanionBinding
import com.bravedroid.jobby.presentation.features.jobs.JobsViewModel
import com.bravedroid.jobby.presentation.util.PageState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class CompanionActivity : AppCompatActivity() {

    private  val viewModel: JobsViewModel by viewModels()

    @Inject
    internal lateinit var jobsAdapter: JobsAdapter

    private lateinit var binding: ActivityCompanionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_companion)
        binding = ActivityCompanionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.jobsRecycler.adapter = jobsAdapter
        binding.jobsRecycler.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false,
        )

        viewModel.uiEventFlow.onEach {
            when (it) {
                JobsViewModel.UiEvent.ContentLoaded -> {
                    Toast.makeText(
                        this,
                        "the content is loaded ",
                        LENGTH_SHORT
                    ).show()
                }
                is JobsViewModel.UiEvent.ShowError -> {
                    Snackbar.make(
                        binding.root,
                        it.errorMessage,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }.launchIn(lifecycleScope)

        lifecycleScope.launchWhenCreated {
            viewModel.pageStateFlow.collectLatest {
                when (it) {
                    is PageState.Content -> {
                        jobsAdapter.submitList(it.data.remoteJobs)
                        binding.loadingBar.isVisible = false
                    }
                    PageState.Error -> {
                        binding.loadingBar.isVisible = false
                    }
                    PageState.Loading -> {
                        binding.loadingBar.isVisible = true
                    }
                }
            }
        }
    }
}
