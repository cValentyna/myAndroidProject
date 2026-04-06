package com.project.myapp.screens.contacts

import androidx.recyclerview.widget.DiffUtil
import com.project.myapp.data.contacts.User

object UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(
        oldItem: User,
        newItem: User,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: User,
        newItem: User,
    ) = oldItem == newItem
}
