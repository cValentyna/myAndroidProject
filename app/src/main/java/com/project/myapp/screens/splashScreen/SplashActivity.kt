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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeGrayStatusBar()
        setStatesCollector()
    }

    private fun setStatesCollector() {
        lifecycleScope.launch(Dispatchers.Default) {
            viewModel.getCachedCredentials.collect { cashedCredentials ->
                when (cashedCredentials) {
                    GetCachedCredentials.Initial -> {}
                    is GetCachedCredentials.Success -> goToMainActivity(cashedCredentials.savedName)
                    is GetCachedCredentials.Fail -> goToAuthActivity()
                }
            }
        }
    }

    private fun goToMainActivity(savedName: String) {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        intent.putExtra(
            USER_NAME_KEY,
            savedName,
        )
        startActivity(intent, customAnimationForward().toBundle())
        finish()
    }

    private fun goToAuthActivity() {
        val intent = Intent(this@SplashActivity, AuthActivity::class.java)
        startActivity(intent, customAnimationForward().toBundle())
        finish()
    }
}
