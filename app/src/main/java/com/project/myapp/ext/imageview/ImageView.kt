package com.project.myapp.ext.imageview

import android.widget.ImageView
import com.project.myapp.imageloader.ImageLibrary
import com.project.myapp.imageloader.createLoader

fun ImageView.loadImage(
    url: String,
    placeholder: Int,
    library: ImageLibrary
) {
    library.createLoader().load(this, url, placeholder)
}
