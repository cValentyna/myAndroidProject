package com.project.myapp.screens.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.project.myapp.DataStore
import com.project.myapp.DataStoreKeys.USER_NAME
import com.project.myapp.ExtensionUtil.setEnableEdgeToEdge
import com.project.myapp.ExtensionUtil.setVisualize
import com.project.myapp.R
import com.project.myapp.Validation
import com.project.myapp.databinding.ActivityAuthBinding
import com.project.myapp.screens.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AuthActivity : AppCompatActivity() {
    private val binding: ActivityAuthBinding by lazy {
        ActivityAuthBinding.inflate(layoutInflater)
    }
    private val dataStore: DataStore by lazy {
        DataStore(this)
    }
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
        setFocusListener()
        setTextChangedListener()
        setOnClickListener()
    }

    override fun onStart() {
        super.onStart()
        goToNextActivityIfUserSaved()
    }

    private fun setView() {
        this.setEnableEdgeToEdge()
        setContentView(binding.root)
        binding.root.setVisualize()
    }

    /* Processes register button
    if email and password are correct - starts next activity and gives name from email
    else shows errors
     * */
    private fun setOnClickListener() {
        binding.apply {
            buttonAuthRegister.setOnClickListener {
                validateEmail()
                validatePassword()
                if (isEmailAndPasswordCorrect()) {
                    val userName = getName(textInputEditTextAuthEmail.text.toString())

                    if (checkboxAuth.isChecked) {
                        saveUser(
                            checkboxAuth.isChecked,
                            textInputEditTextAuthEmail.text.toString(),
                            textInputEditTextAuthPassword.text.toString(),
                            userName,
                        )
                    }

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
      Saves user information if necessary
     */
    private fun saveUser(
        isChecked: Boolean,
        email: String,
        password: String,
        name: String,
    ) {
        lifecycleScope.launch {
            dataStore.saveData(isChecked, email, password, name)
        }
    }

    /*
     If user was saved goes to MainActivity
     */

    private fun goToNextActivityIfUserSaved() {
        lifecycleScope.launch {
            // runBlocking added to prevent  AuthActivity screen from being shown if the user has been saved
            if (runBlocking { dataStore.getWasChecked().first() }) {
                val intent = Intent(this@AuthActivity, MainActivity::class.java)
                intent.putExtra(
                    "userName",
                    runBlocking { dataStore.getSavedSting(USER_NAME).first() },
                )
                startActivity(intent)
                finish()
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
                    validateEmail()
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
                    validatePassword()
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
            textInputEditTextAuthEmail.doOnTextChanged { _, _, _, _ ->
                if (viewModel.authState.value.wasFocusEmail) {
                    validateEmail()
                } else {
                    textInputLayoutAuthEmail.helperText = null
                }
            }
            textInputEditTextAuthPassword.doOnTextChanged { _, _, _, _ ->
                if (viewModel.authState.value.wasFocusPassword || viewModel.authState.value.wasRegisterButtonClicked) {
                    validatePassword()
                }
            }
        }
    }

    private fun validateEmail() {
        binding.apply {
            val helperText =
                Validation.validateEmail(
                    textInputEditTextAuthEmail.text.toString(),
                    this@AuthActivity,
                )
            textInputLayoutAuthEmail.helperText = helperText
            if (helperText != null) {
                viewModel.updateState {
                    copy(isUserEmailValid = false)
                }
            } else {
                viewModel.updateState {
                    copy(isUserEmailValid = true)
                }
            }
        }
    }

    private fun validatePassword() {
        binding.apply {
            val helperText =
                Validation.validatePassword(
                    textInputEditTextAuthPassword.text.toString(),
                    this@AuthActivity,
                )
            textInputLayoutAuthPassword.helperText = helperText
            if (helperText != null) {
                viewModel.updateState {
                    copy(isUserPasswordValid = false)
                }
            } else {
                viewModel.updateState {
                    copy(isUserPasswordValid = true)
                }
            }
        }
    }

    private fun showErrorIfEmpty() {
        binding.apply {
            if (textInputEditTextAuthEmail.text.toString().isEmpty()) {
                textInputLayoutAuthEmail.helperText =
                    Validation.emptyEmail(
                        textInputEditTextAuthEmail.text.toString(),
                        this@AuthActivity,
                    )
                viewModel.updateState {
                    copy(isUserEmailValid = false)
                }
            }

            if (textInputEditTextAuthPassword.text.toString().isEmpty()) {
                textInputLayoutAuthPassword.helperText =
                    Validation.emptyPassword(
                        textInputEditTextAuthPassword.text.toString(),
                        this@AuthActivity,
                    )
                viewModel.updateState {
                    copy(isUserPasswordValid = false)
                }
            }
        }
    }

    private fun isEmailAndPasswordCorrect(): Boolean =
        viewModel.authState.value.isUserEmailValid &&
            viewModel.authState.value.isUserPasswordValid &&
            binding.textInputEditTextAuthEmail.text
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
            .joinToString(" ") { it ->
                it.lowercase().replaceFirstChar { it.uppercaseChar() }
            }
}
