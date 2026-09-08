package com.project.myapp.data.contacts
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    val id: Int,
    val name: String,
    val profession: String,
    val photoUrl: String,
) : Parcelable
