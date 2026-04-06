package com.project.myapp.screens.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.project.myapp.databinding.ContactDialogBinding
import com.project.myapp.ext.string.capitalizeFirstLetter
import com.project.myapp.validation.NameValidator
import com.project.myapp.validation.ValidationResult


class ContactDialogFragment : DialogFragment() {
    private var _binding: ContactDialogBinding? = null
    private val binding get() = _binding!!

    private val nameValidator = NameValidator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ContactDialogBinding.inflate(inflater, container, false)

        val savedHelperText = savedInstanceState?.getString(KEY_SAVEDINSTANCE_STATE)
        binding.textInputLayoutContactUserName.helperText = savedHelperText

        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
        binding.buttonSaveUser.setOnClickListener {
            saveUser()
        }
    }

    private fun saveUser() {
        with(binding) {
            val contactName = textInputEditTextUserName.text.toString().trim()
            val contactLastName = textInputEditTextUserLastName.text.toString().trim()
            val profession = textInputEditTextUserProfession.text.toString().trim()

            val result = nameValidator.validateName(contactName)

            if (result is ValidationResult.Error) {
                textInputLayoutContactUserName.helperText = getString(result.messageRId)
                return
            }
            val fullName = "${contactName.capitalizeFirstLetter()} ${contactLastName.capitalizeFirstLetter()}"

            val bundle = bundleOf(KEY_NAME to fullName, KEY_PROFESSION to profession.capitalizeFirstLetter())

            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundle)
            dismiss()
        }
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
    }
}
