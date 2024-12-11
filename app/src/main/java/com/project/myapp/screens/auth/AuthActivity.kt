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
    }

    private fun setOnClickListener() {
        binding.apply {
            buttonAuthRegister.setOnClickListener {
                processingAuthRegisterButton()
            }
        }
    }

    /**
     * Processes register button -  validates credentials if they are incorrect:
     * shows errors, else starts next activity and gives name from email
     */
    private fun processingAuthRegisterButton() {
        validateCredentials()
        if (viewModel.isEmailCorrect() && viewModel.isPasswordCorrect()) {
            goToNextActivity()
        } else {
            this@AuthActivity.toast(getString(R.string.error_invalid_email_or_password))
        }
    }

    /**
     * Validates authorisation data after click registration button
     */
    private fun validateCredentials() {
        viewModel.updateRegistrationButtonClicked()
        binding.apply {
            textInputLayoutAuthEmail.helperText =
                viewModel.checkEmailByClick(textInputEditTextAuthEmail.text.toString())
                    ?.let { getString(it) }
            textInputLayoutAuthPassword.helperText =
                viewModel.checkPasswordByClick(textInputEditTextAuthPassword.text.toString())
                    ?.let { getString(it) }
        }
    }

    /**
     * Starts new activity
     */
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

    /** This method validates email and password if their InputEditText was focused,
     * but lost it.
     */
    private fun setFocusListener() {
        binding.apply {
            textInputEditTextAuthEmail.setOnFocusChangeListener { v, hasFocus ->
                viewModel.focusEmailUpdate(hasFocus, v.isFocused)
                if (viewModel.wasEmailFocus()) {
                    validateEmail()
                }
            }
            textInputEditTextAuthPassword.setOnFocusChangeListener { v, hasFocus ->
                viewModel.focusPasswordUpdate(hasFocus, v.isFocused)
                if (viewModel.wasPasswordFocus()) {
                    validatePassword()
                }
            }
        }
    }

    /** If register button was pressed or textInputEditText was focused and lost focus,
     * error will be shown if input text did not pass validation
     * when user is correcting:
     * email - error will not be shown
     * password - error will be shown until password is correct
     */
    private fun setTextChangedListener() {
        binding.apply {
            textInputEditTextAuthEmail.doOnTextChanged { _, _, _, _ ->
                if (viewModel.wasEmailFocus()) {
                    validateEmail()
                } else {
                    textInputLayoutAuthEmail.helperText = null
                }
            }
            textInputEditTextAuthPassword.doOnTextChanged { _, _, _, _ ->
                if (viewModel.wasPasswordFocus() || viewModel.wasRegisterButtonClicked()) {
                    validatePassword()
                }
            }
        }
    }

    private fun validateEmail() {
        binding.apply {
            textInputLayoutAuthEmail.helperText =
                viewModel.validateEmail(textInputEditTextAuthEmail.text.toString())
                    ?.let { getString(it) }
        }
    }

    private fun validatePassword() {
        binding.apply {
            textInputLayoutAuthPassword.helperText =
                viewModel.validatePassword(textInputEditTextAuthPassword.text.toString())
                    ?.let { getString(it) }
        }
    }
}
