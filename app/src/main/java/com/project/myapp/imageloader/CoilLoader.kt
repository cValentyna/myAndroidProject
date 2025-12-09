package com.project.myapp.imageloader

import android.util.Log
import android.widget.ImageView
import coil.load
import coil.size.Precision
import coil.transform.CircleCropTransformation
import javax.inject.Inject

class CoilLoader @Inject constructor() : ImageLoader {
    override fun load( imageView: ImageView, url: String, placeholder: Int) {
        Log.d("ImageDebug", "CoilLoader.load image")
        imageView.load(url) {
            placeholder(placeholder)
            error(placeholder)
            precision(Precision.INEXACT)
            transformations(CircleCropTransformation())
        }
    }
}
