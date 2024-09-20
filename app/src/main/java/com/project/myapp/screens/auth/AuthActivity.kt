package com.project.myapp.screens.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.doOnTextChanged
import com.project.myapp.R
import com.project.myapp.databinding.ActivityAuthBinding
import com.project.myapp.screens.main.MainActivity

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

    /* Processes register button
    if email and password are correct - starts next activity and gives name from email
    else shows errors
     * */
    private fun setOnClickListener() {
        binding.apply {
            buttonAuthRegister.setOnClickListener {
                validateEmail(textInputEditTextAuthEmail.text)
                validatePassword(textInputEditTextAuthPassword.text)
                if (isEmailAndPasswordCorrect()) {
                    val userName = getName(textInputEditTextAuthEmail.text.toString())
                    val intent = Intent(this@AuthActivity, MainActivity::class.java)
                    val option =
                        ActivityOptionsCompat.makeCustomAnimation(
                            this@AuthActivity,
                            R.anim.slide_in_left,
                            R.anim.slide_out_left,
                        )
                    intent.putExtra("userName", userName)
                    startActivity(intent, option.toBundle())
                    finish()
                } else {
                    viewModel.updateState {
                        copy(wasRegisterButtonClicked = true)
                    }
                    Toast
                        .makeText(
                            this@AuthActivity,
                            getString(R.string.error_invalid_email_or_password),
                            Toast.LENGTH_SHORT,
                        ).show()
                    showErrorIfEmpty()
                }
            }
        }
    }

    /*
    This method validates email and password if their InputEditText was focused,
    but lost it.
     */
    private fun setFocusListener() {
        binding.apply {
            textInputEditTextAuthEmail.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    viewModel.updateState {
                        copy(firstFocusEmail = true, wasFocusEmail = false)
                    }
                } else if (!v.isFocused && viewModel.authState.value.firstFocusEmail) {
                    viewModel.updateState {
                        copy(wasFocusEmail = true)
                    }
                }
                if (viewModel.authState.value.wasFocusEmail) {
                    validateEmail(textInputEditTextAuthEmail.text)
                }
            }
            textInputEditTextAuthPassword.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    viewModel.updateState {
                        copy(firstFocusPassword = true)
                    }
                } else if (!v.isFocused && viewModel.authState.value.firstFocusPassword) {
                    viewModel.updateState {
                        copy(wasFocusPassword = true)
                    }
                }
                if (viewModel.authState.value.wasFocusPassword) {
                    validatePassword(textInputEditTextAuthPassword.text.toString())
                }
            }
        }
    }

    /*
    If register button was pressed or textInputEditText was focused and lost focus,
    error will be shown if input text did not pass validation
    when user is correcting:
    email - error will not be shown
    password - error will be shown until password is correct
     */
    private fun setTextChangedListener() {
        binding.apply {
            textInputEditTextAuthEmail.doOnTextChanged { text: CharSequence?, _, _, _ ->
                if (viewModel.authState.value.wasFocusEmail) {
                    validateEmail(text)
                } else {
                    textInputLayoutAuthEmail.helperText = null
                }
            }
            textInputEditTextAuthPassword.doOnTextChanged { text: CharSequence?, _, _, _ ->
                if (viewModel.authState.value.wasFocusPassword || viewModel.authState.value.wasRegisterButtonClicked) {
                    validatePassword(text)
                }
            }
        }
    }

    private fun validateEmail(text: CharSequence?) {
        val pattern = Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
        if (!pattern && text.toString().isNotEmpty()) {
            viewModel.updateState {
                copy(isUserEmailValid = false)
            }
            binding.textInputLayoutAuthEmail.helperText = getString(R.string.error_e_mail_address)
        } else {
            viewModel.updateState {
                copy(isUserEmailValid = true)
            }
            binding.textInputLayoutAuthEmail.helperText = null
        }
    }

    private fun validatePassword(text: CharSequence?) {
        // pattern if special_symbol can be present
        val pattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\\w#?!@\$%^&*-]{8,}$")
        val patternExpectedSymbols = Regex("^[\\w#?!@\$%^&*-]+\$")
        val patternDigit = Regex("(?=.*[0-9])")
        val patternLetterLC = Regex("(?=.*[a-z])")
        val patternLetterUC = Regex("(?=.*[A-Z])")
        val patternCount = Regex("([\\w#?!@\$%^&*-]{8,})")

        if (!pattern.containsMatchIn(text.toString()) && text.toString().isNotEmpty()) {
            viewModel.updateState {
                copy(isUserPasswordValid = false)
            }
            when {
                !patternExpectedSymbols.containsMatchIn(text.toString()) ->
                    binding.textInputLayoutAuthPassword.helperText =
                        getString(R.string.error_password_unpredictable_symbols)

                !patternLetterLC.containsMatchIn(text.toString()) ->
                    binding.textInputLayoutAuthPassword.helperText =
                        getString(R.string.error_password_lower_case)

                !patternLetterUC.containsMatchIn(text.toString()) ->
                    binding.textInputLayoutAuthPassword.helperText =
                        getString(R.string.error_password_upper_case)

                !patternDigit.containsMatchIn(text.toString()) ->
                    binding.textInputLayoutAuthPassword.helperText =
                        getString(R.string.error_password_digit)

                !patternCount.containsMatchIn(text.toString()) ->
                    binding.textInputLayoutAuthPassword.helperText =
                        getString(R.string.error_password_minimum_characters)
            }
        } else {
            binding.textInputLayoutAuthPassword.helperText = null
            viewModel.updateState {
                copy(isUserPasswordValid = true)
            }
        }
    }

    private fun showErrorIfEmpty() {
        binding.apply {
            if (textInputEditTextAuthEmail.text.toString().isEmpty()
            ) {
                viewModel.updateState {
                    copy(isUserEmailValid = false)
                }
                textInputLayoutAuthEmail.helperText = getString(R.string.error_email_empty)
            }
            if (textInputEditTextAuthPassword.text.toString().isEmpty()
            ) {
                viewModel.updateState {
                    copy(isUserPasswordValid = false)
                }
                textInputLayoutAuthPassword.helperText = getString(R.string.error_password_empty)
            }
        }
    }

    private fun isEmailAndPasswordCorrect(): Boolean =
        viewModel.authState.value.isUserEmailValid &&
            viewModel.authState.value.isUserPasswordValid &&
            binding.textInputEditTextAuthPassword.text
                .toString()
                .isNotEmpty() &&
            binding.textInputEditTextAuthPassword.text
                .toString()
                .isNotEmpty()

    /* Receives name from Email
     */
    private fun getName(email: String): String =
        email
            .substringBefore("@")
            .split(".", "_")
            .joinToString(" ") { it -> it.lowercase().replaceFirstChar { it.uppercaseChar() } }
}
