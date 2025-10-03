package com.project.myapp.imageloader

import android.widget.ImageView

interface ImageLoader {
    fun load(
        imageView: ImageView,
        url: String,
        placeholder: Int,
    )
}
