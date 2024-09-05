package com.project.myapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.project.myapp.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private val binding: ActivityAuthBinding by lazy {
        ActivityAuthBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // removed ViewCompat.setOnApplyWindowInsetsListener

        // processes Registration button
        setOnClickListener()
        /* shows errors just for visualize. They have not validation yet
         The error still appears when the user starts typing
         * */
        setTextChangedListener()
    }

    private fun setOnClickListener() {
        binding.apply {
            buttonAuthRegister.setOnClickListener {
                val intent = Intent(this@AuthActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setTextChangedListener() {
        binding.apply {
            textInputEditTextAuthEmail.doOnTextChanged { text: CharSequence?, _, _, _ ->
                if (!text.isNullOrEmpty() || textInputEditTextAuthEmail.text.toString() != "email") {
                    textInputLayoutAuthEmail.helperText =
                        getString(R.string.error_validation_email)
                } else {
                    textInputLayoutAuthEmail.helperText = null
                }
            }
            textInputEditTextAuthPassword.doOnTextChanged { text: CharSequence?, _, _, _ ->
                if (!text.isNullOrEmpty()) {
                    textInputLayoutAuthPassword.helperText =
                        getString(R.string.error_validation_password)
                } else {
                    textInputLayoutAuthPassword.helperText = null
                }
            }
        }
    }
}
