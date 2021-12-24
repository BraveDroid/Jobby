package com.bravedroid.jobby.companion.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bravedroid.jobby.companion.R
import com.bravedroid.jobby.companion.databinding.ActivityDemoBinding
import com.bravedroid.jobby.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DemoActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityDemoBinding.inflate(layoutInflater)
        val binding = ActivityDemoBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainerView.id, LoginFragment(), "LOGIN")
            .commit()

    }
}
