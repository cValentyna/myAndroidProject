package com.project.myapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.project.myapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getName()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.apply {
            buttonMainLogout.setOnClickListener {
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
