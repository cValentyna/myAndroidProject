package com.project.myapp.screens.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.myapp.R
import com.project.myapp.databinding.ContactItemBinding
import com.project.myapp.ext.imageview.loadImage
import com.project.myapp.imageloader.ImageLibrary

class ContactAdapter(
    private var contacts: List<User> = emptyList(),
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    class ContactViewHolder(
        private val binding: ContactItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val currentLibrary = ImageLibrary.GLIDE // can be GLIDE, PICASSO, COIL

        fun bind(user: User) {
            binding.contactName.text =
                buildString {
                    append(user.name)
                    append(" ")
                    append(user.id)
                }
            binding.contactProfession.text = user.profession
            val imageView = binding.contactImageView
            imageView.loadImage(user.photoUrl, R.drawable.round_icon, currentLibrary)
        }
    }

    fun update(newList: List<User>) {
        contacts = newList.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ContactViewHolder {
        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ContactViewHolder,
        position: Int,
    ) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size
}
