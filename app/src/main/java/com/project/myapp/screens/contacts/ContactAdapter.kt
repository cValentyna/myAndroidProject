package com.project.myapp.screens.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.myapp.R
import com.project.myapp.data.contacts.User
import com.project.myapp.databinding.ContactItemBinding
import com.project.myapp.imageloader.ImageLoader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactAdapter @Inject constructor(
    private val imageLoader: ImageLoader
) : ListAdapter<User, ContactAdapter.ContactViewHolder>(UserDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ContactViewHolder {
        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding, imageLoader)
    }

    override fun onBindViewHolder(
        holder: ContactViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    class ContactViewHolder(
        private val binding: ContactItemBinding,
        private val imageLoader: ImageLoader,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            val name = "${user.name} ${user.id}"
            binding.contactName.text = name
            binding.contactProfession.text = user.profession
            val imageView = binding.contactImageView
            imageLoader.load(imageView, user.photoUrl, R.drawable.round_icon)
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(
            oldItem: User,
            newItem: User,
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: User,
            newItem: User,
        ): Boolean = oldItem == newItem
    }

    fun update(newList: List<User>) {
        submitList(newList)
    }
}
