package com.bravedroid.jobby.companion.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bravedroid.jobby.companion.databinding.ActivityDemoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DemoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
