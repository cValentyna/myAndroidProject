package com.project.myapp.screens.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.project.myapp.KeysHolder.USER_NAME_KEY
import com.project.myapp.databinding.ActivityMainBinding
import com.project.myapp.ext.componentactivity.EnableEdgeToEdgeGrayStatusBar
import com.project.myapp.ext.componentactivity.handleBackPress
import com.project.myapp.ext.context.customAnimationBackward
import com.project.myapp.ext.view.initializeWindowInsetsHandling
import com.project.myapp.screens.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
        setName()
        setOnClickListener()
        handleBackPress()
    }

    private fun setView() {
        EnableEdgeToEdgeGrayStatusBar()
        setContentView(binding.root)
        binding.root.initializeWindowInsetsHandling()
    }

    private fun setOnClickListener() {
        binding.buttonMainLogout.setOnClickListener {
            lifecycleScope.launch {
                viewModel.forgetUser()
            }
            goToPreviousActivity()
        }
    }

    private fun goToPreviousActivity() {
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        startActivity(intent, customAnimationBackward().toBundle())
        finish()
    }

    private fun setName() {
        binding.textViewMainUserName.text = intent.getStringExtra(USER_NAME_KEY)
    }
}
