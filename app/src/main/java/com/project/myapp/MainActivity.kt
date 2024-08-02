package com.project.myapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.project.myapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvUserNameMain.text=intent.getStringExtra("userName")

        // processes log-out button return
        binding.btLogOutMain.setOnClickListener {
            val intent = Intent(this@MainActivity, AuthActivity::class.java)
            val option = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.slide_in_right,
                R.anim.slide_out_right
            )
            startActivity(intent, option.toBundle())
            finish()
        }


    }
}