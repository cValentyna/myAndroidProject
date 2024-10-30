package com.project.myapp.screens.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.project.myapp.DataStore
import com.project.myapp.ExtensionUtil.setEnableEdgeToEdge
import com.project.myapp.ExtensionUtil.setVisualize
import com.project.myapp.R
import com.project.myapp.databinding.ActivityMainBinding
import com.project.myapp.screens.auth.AuthActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val dataStore: DataStore by lazy {
        DataStore(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
        getName()
        setOnClickListener()
    }

    private fun setView() {
        this.setEnableEdgeToEdge()
        setContentView(binding.root)
        binding.root.setVisualize()
    }

    private fun setOnClickListener() {
        binding.apply {
            buttonMainLogout.setOnClickListener {
                lifecycleScope.launch {
                    dataStore.clearPreferences()
                }
                val intent = Intent(this@MainActivity, AuthActivity::class.java)
                val option =
                    ActivityOptionsCompat.makeCustomAnimation(
                        this@MainActivity,
                        R.anim.slide_in_right,
                        R.anim.slide_out_right,
                    )
                startActivity(intent, option.toBundle())
                finish()
            }
        }
    }

    private fun getName() {
        binding.textViewMainUserName.text = intent.getStringExtra("userName")
    }
}
