package com.project.myapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.project.myapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var pref:Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = Preferences(this)

        binding.tvUserNameMain.text=intent.getStringExtra("userName")

        // changes default image
        binding.imageView.setImageResource(R.drawable.bee)

        // processes log-out button return
        binding.btLogOutMain.setOnClickListener {
            pref.clearPreferences()
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