package com.project.myapp.imageloader

enum class ImageLibrary {
    GLIDE,
    PICASSO,
    COIL,
}

fun ImageLibrary.createLoader(): ImageLoader =
    when (this) {
        ImageLibrary.GLIDE -> GlideLoader()
        ImageLibrary.PICASSO -> PicassoLoader()
        ImageLibrary.COIL -> CoilLoader()
    }
