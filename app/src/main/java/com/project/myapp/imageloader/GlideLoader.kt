package com.project.myapp.imageloader

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import javax.inject.Inject

class GlideLoader @Inject constructor(): ImageLoader {
    override fun load(imageView: ImageView, url: String, placeholder: Int) {
        Log.d("ImageDebug", "GlideLoader.load image")
        Glide
            .with(imageView)
            .load(url)
            .placeholder(placeholder)
            .error(placeholder)
            .circleCrop()
            .into(imageView)
    }
}
