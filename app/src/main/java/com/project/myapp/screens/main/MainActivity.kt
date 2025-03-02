package com.project.myapp.screens.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.project.myapp.KeysHolder.USER_NAME_KEY
import com.project.myapp.R
import com.project.myapp.databinding.ActivityMainBinding
import com.project.myapp.screens.auth.AuthActivity

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setName()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.buttonMainLogout.setOnClickListener {
            goToPreviousActivity()
        }
    }

    private fun goToPreviousActivity() {
        val animation =
            ActivityOptionsCompat.makeCustomAnimation(
                this@MainActivity,
                R.anim.slide_in_right,
                R.anim.slide_out_right,
            )
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        startActivity(intent, animation.toBundle())
        finish()
    }

    private fun setName() {
        binding.textViewMainUserName.text = intent.getStringExtra(USER_NAME_KEY)
    }
}
