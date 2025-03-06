package com.project.myapp.screens.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.myapp.KeysHolder.USER_NAME_KEY
import com.project.myapp.databinding.ActivityMainBinding
import com.project.myapp.ext.componentactivity.handleBackPress
import com.project.myapp.ext.componentactivity.setEnableEdgeToEdge
import com.project.myapp.ext.context.customAnimationBackward
import com.project.myapp.ext.view.setVisualize
import com.project.myapp.screens.auth.AuthActivity

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
        setName()
        setOnClickListener()
        handleBackPress()
    }

    private fun setView() {
        setEnableEdgeToEdge()
        setContentView(binding.root)
        binding.root.setVisualize()
    }

    private fun setOnClickListener() {
        binding.buttonMainLogout.setOnClickListener {
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
