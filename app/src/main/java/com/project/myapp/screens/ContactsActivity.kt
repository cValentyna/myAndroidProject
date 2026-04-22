package com.project.myapp.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.myapp.databinding.ActivityContactsBinding
import com.project.myapp.ext.componentactivity.enableEdgeToEdgeGrayStatusBar
import com.project.myapp.ext.view.initializeWindowInsetsHandling
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsActivity : AppCompatActivity() {
    private val binding: ActivityContactsBinding by lazy {
        ActivityContactsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
    }

    private fun setView() {
        enableEdgeToEdgeGrayStatusBar()
        setContentView(binding.root)
        binding.root.initializeWindowInsetsHandling()
    }
}
