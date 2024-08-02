package com.project.myapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.myapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // processes log-out button return
        binding.btLogOutMain.setOnClickListener {
            val intent = Intent(this@MainActivity, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}