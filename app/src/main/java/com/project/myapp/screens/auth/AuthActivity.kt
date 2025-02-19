package com.project.myapp.screens.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.doOnTextChanged
import com.project.myapp.KeysHolder.USER_NAME_KEY
import com.project.myapp.R
import com.project.myapp.databinding.ActivityAuthBinding
import com.project.myapp.screens.main.MainActivity
import com.project.myapp.toast

class AuthActivity : AppCompatActivity() {
    private val binding: ActivityAuthBinding by lazy {
        ActivityAuthBinding.inflate(layoutInflater)
    }
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setFocusListener()
        setTextChangedListener()
        setOnClickListener()
        setObservers()
    }

    /**
     * Sets clickListener
     * when button is clicked - validates credentials
     */
    private fun setOnClickListener() {
        binding.apply {
            buttonAuthRegister.setOnClickListener {
                viewModel.checkCredentials(
                    textInputEditTextAuthEmail.text.toString(),
                    textInputEditTextAuthPassword.text.toString(),
                )
            }
        }
    }

    /**
     * Sets TextChangedListeners
     * pauses email and password validation when text inside fields changes
     */

    private fun setTextChangedListener() {
        binding.apply {
            textInputEditTextAuthEmail.doOnTextChanged { _, _, _, _ ->
                viewModel.pauseCheckEmail()
            }
            textInputEditTextAuthPassword.doOnTextChanged { _, _, _, _ ->
                viewModel.pauseCheckPassword()
            }
        }
    }

    /**
     * Sets focusListeners
     * validates email and password when they lost focus
     */
    private fun setFocusListener() {
        binding.apply {
            textInputEditTextAuthEmail.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    viewModel.validateEmail(textInputEditTextAuthEmail.text.toString())
                }
            }
            textInputEditTextAuthPassword.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    viewModel.validatePassword(textInputEditTextAuthPassword.text.toString())
                }
            }
        }
    }

    /**
     * Sets Observers
     *
     */
    private fun setObservers() {
        viewModel.passwordState.observe(this) { state ->
            binding.textInputLayoutAuthPassword.error =
                when (state) {
                    is AuthState.PasswordState.Error -> state.message?.let { getString(it) }
                    is AuthState.PasswordState.Empty -> state.message?.let { getString(it) }
                    is AuthState.PasswordState.InvisibleError,
                    is AuthState.PasswordState.Valid -> null
                }
        }

        viewModel.emailState.observe(this) { state ->
            binding.textInputLayoutAuthEmail.error =
                when (state) {
                    is AuthState.EmailState.Error -> state.message?.let { getString(it) }
                    is AuthState.EmailState.Empty -> state.message?.let { getString(it) }
                    is AuthState.EmailState.InvisibleError,
                    is AuthState.EmailState.Valid
                    -> null
                }
        }

        viewModel.credentialsState.observe(this) { state ->
            when (state) {
                is AuthState.Error ->
                    this.toast(state.message?.let { getString(it) }.toString())
                is AuthState.Valid -> goToNextActivity()
            }
        }
    }


    private fun goToNextActivity() {
        val userName = viewModel.getName(binding.textInputEditTextAuthEmail.text.toString())
        val intent = Intent(this@AuthActivity, MainActivity::class.java)
        val option =
            ActivityOptionsCompat.makeCustomAnimation(
                this@AuthActivity,
                R.anim.slide_in_left,
                R.anim.slide_out_left,
            )
        intent.putExtra(USER_NAME_KEY, userName)
        startActivity(intent, option.toBundle())
        finish()
    }
}
