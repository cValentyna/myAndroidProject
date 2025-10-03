package com.project.myapp.imageloader

import android.widget.ImageView
import com.bumptech.glide.Glide
import javax.inject.Inject

class GlideLoader @Inject constructor(): ImageLoader {
    override fun load(imageView: ImageView, url: String, placeholder: Int) {
        Glide
            .with(imageView)
            .load(url)
            .placeholder(placeholder)
            .error(placeholder)
            .circleCrop()
            .into(imageView)
    }
}
