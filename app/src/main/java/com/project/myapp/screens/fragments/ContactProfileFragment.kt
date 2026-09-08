package com.project.myapp.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.myapp.R
import com.project.myapp.databinding.FragmentContactProfileBinding
import com.project.myapp.imageloader.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactProfileFragment : Fragment() {
    private var _binding: FragmentContactProfileBinding? = null
    private val binding get() = _binding!!
    private val args: ContactProfileFragmentArgs by navArgs()

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentContactProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = args.user.name
        val profession = args.user.profession
        val photoUrl = args.user.photoUrl

        binding.apply {
            textViewProfileUserName.text = name
            textViewProfileUserJob.text = profession
            imageLoader.load(contactImageView, photoUrl, R.drawable.round_icon)
        }
        setClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setClickListeners() {
        binding.contactBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
