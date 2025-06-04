package com.project.myapp.screens.splashScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.project.myapp.KeysHolder.USER_NAME_KEY
import com.project.myapp.ext.componentactivity.enableEdgeToEdgeGrayStatusBar
import com.project.myapp.ext.context.customAnimationForward
import com.project.myapp.screens.auth.AuthActivity
import com.project.myapp.screens.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeGrayStatusBar()
        lifecycleScope.launch {
            if (viewModel.wasSavedUser()) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.putExtra(
                    USER_NAME_KEY,
                    viewModel.getSavedName(),
                )
                startActivity(intent, customAnimationForward().toBundle())
                finish()
            } else {
                val intent = Intent(this@SplashActivity, AuthActivity::class.java)
                startActivity(intent, customAnimationForward().toBundle())
                finish()
            }
        }
    }
}
