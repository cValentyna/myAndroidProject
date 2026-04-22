package com.project.myapp.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.project.myapp.R
import com.project.myapp.databinding.FragmentContactProfileBinding
import com.project.myapp.imageloader.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactProfileFragment : Fragment(R.layout.fragment_contact_profile) {
    private var _binding: FragmentContactProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentContactProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString(USER_NAME)
        val profession = arguments?.getString(USER_PROFESSION)
        val photoUrl = arguments?.getString(USER_PHOTO)

        binding.apply {
            textViewProfileUserName.text = name
            textViewProfileUserJob.text = profession
            imageLoader.load(contactImageView, photoUrl.toString(), R.drawable.round_icon)
        }
        setClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setClickListeners() {
        binding.contactBackButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        const val USER_NAME = "user_info"
        const val USER_PROFESSION = "user_profession"
        const val USER_PHOTO = "user_photo"

        fun newInstance(
            name: String,
            profession: String,
            photoUrl: String,
        ) = ContactProfileFragment().apply {
            arguments =
                bundleOf(USER_NAME to name, USER_PROFESSION to profession, USER_PHOTO to photoUrl)
        }
    }
}
