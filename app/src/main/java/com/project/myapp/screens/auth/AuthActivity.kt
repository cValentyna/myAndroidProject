package com.project.myapp.screens.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.project.myapp.KeysHolder.USER_NAME_KEY
import com.project.myapp.R
import com.project.myapp.databinding.ActivityAuthBinding
import com.project.myapp.ext.toast
import com.project.myapp.screens.main.MainActivity
import kotlinx.coroutines.launch

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
        setStatesCollectors()
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
     * pauses email and password validation and hides error if it was shown
     * when text inside fields changes
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
     * Sets collectors for monitored states
     */
    private fun setStatesCollectors() {
        lifecycleScope.launch {
            viewModel.passwordState.collect { state ->
                binding.textInputLayoutAuthPassword.helperText =
                    when (state) {
                        is AuthState.PasswordState.ErrorInvalidSign ->
                            getString(R.string.error_password_unpredictable_symbols)

                        is AuthState.PasswordState.ErrorLessCharacters ->
                            getString(R.string.error_password_minimum_characters)

                        is AuthState.PasswordState.ErrorNoNumber ->
                            getString(R.string.error_password_digit)

                        is AuthState.PasswordState.ErrorNoLetter ->
                            getString(R.string.error_password_letters)

                        is AuthState.PasswordState.ErrorEmpty ->
                            getString(R.string.error_password_empty)
                        else -> null
                    }
            }
        }

        lifecycleScope.launch {
            viewModel.emailState.collect { state ->
                binding.textInputLayoutAuthEmail.helperText =
                    when (state) {
                        is AuthState.EmailState.Error -> getString(R.string.error_incorrect_e_mail_address)
                        is AuthState.EmailState.ErrorEmpty -> getString(R.string.error_email_empty)
                        else -> null
                    }
            }
        }

        lifecycleScope.launch {
            viewModel.credentialsState.collect { state ->
                when (state) {
                    is AuthState.Error ->
                        toast(getString(R.string.error_invalid_email_or_password))

                    is AuthState.Valid -> goToNextActivity()
                    else -> {}
                }
            }
        }
    }

    private fun goToNextActivity() {
        val userName = viewModel.parseName(binding.textInputEditTextAuthEmail.text.toString())
        val animation =
            ActivityOptionsCompat.makeCustomAnimation(
                this@AuthActivity,
                R.anim.slide_in_left,
                R.anim.slide_out_left,
            )
        val intent =
            Intent(this, MainActivity::class.java).apply {
                putExtra(USER_NAME_KEY, userName)
            }

        startActivity(intent, animation.toBundle())
        finish()
    }
}
