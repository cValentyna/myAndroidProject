package com.project.myapp.screens.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.project.myapp.R
import com.project.myapp.databinding.ContactDialogBinding
import com.project.myapp.ext.string.capitalizeFirstLetter


class ContactDialogFragment : DialogFragment() {
    private var _binding: ContactDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ContactDialogBinding.inflate(inflater, container, false)

        val savedHelperText = savedInstanceState?.getString(KEY_SAVEDINSTANCE_STATE)
        binding.textInputLayoutContactUserName.helperText = savedHelperText

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonSaveUser.setOnClickListener {
            saveUser()
        }

        return binding.root
    }

    private fun saveUser() {
        val contactName = binding.textInputEditTextUserName.text.toString().trim()
        val contactLastName = binding.textInputEditTextUserLastName.text.toString().trim()
        val profession = binding.textInputEditTextUserProfession.text.toString().trim()

        if (!isValidName(contactName)) {
            binding.textInputLayoutContactUserName.helperText =
                when {
                    contactName.isEmpty() -> getString(R.string.error_contact_name_empty)
                    else -> getString(R.string.error_contact_name)
                }
            return
        }

        val fullName =
            if (contactLastName.isNotEmpty()) {
                "${contactName.capitalizeFirstLetter()} ${contactLastName.capitalizeFirstLetter()}"
            } else {
                contactName.capitalizeFirstLetter()
            }

        val bundle =
            Bundle().apply {
                putString(KEY_NAME, fullName)
                putString(KEY_PROFESSION, profession.capitalizeFirstLetter())
            }

        parentFragmentManager.setFragmentResult(REQUEST_KEY, bundle)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SAVEDINSTANCE_STATE, binding.textInputLayoutContactUserName.helperText?.toString())
    }

    companion object {
        const val REQUEST_KEY = "request_data"
        const val KEY_NAME = "new_user_name"
        const val KEY_PROFESSION = "new_user_profession"
        const val KEY_SAVEDINSTANCE_STATE = "helperText"

        private fun isValidName(name: String): Boolean {
            val trimmed = name.trim()

            if (trimmed.isEmpty()) return false
            if (trimmed.length < 2 || trimmed.length > 20) return false

            var hasLetter = false
            var prevWasApostrophe = false
            var prevWasHyphen = false

            for (ch in trimmed) {
                val apostrophes = setOf('\'', '’', 'ʼ', '‘')
                val isApostrophe = ch in apostrophes
                when {
                    ch.isLetter() -> {
                        hasLetter = true
                        prevWasApostrophe = false
                        prevWasHyphen = false
                    }

                    ch == ' ' -> {
                        prevWasApostrophe = false
                        prevWasHyphen = false
                    }

                    ch == '-' -> {
                        if (prevWasHyphen) return false
                        if (prevWasApostrophe) return false
                        prevWasHyphen = true
                        prevWasApostrophe = false
                    }

                    isApostrophe -> {
                        if (prevWasApostrophe) return false
                        if (prevWasHyphen) return false
                        prevWasApostrophe = true
                        prevWasHyphen = false
                    }

                    else -> return false
                }
            }

            return hasLetter
        }
    }
}
