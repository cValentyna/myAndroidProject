package com.project.myapp.imageloader

import javax.inject.Inject
import javax.inject.Singleton

enum class ImageLibrary {
    GLIDE,
    PICASSO,
    COIL,
}

@Singleton
class ImageLibrarySelector @Inject constructor() {
    var chosenLibrary: ImageLibrary = ImageLibrary.PICASSO
}

fun ImageLibrary.createLoader(): ImageLoader =
    when (this) {
        ImageLibrary.GLIDE -> GlideLoader()
        ImageLibrary.PICASSO -> PicassoLoader()
        ImageLibrary.COIL -> CoilLoader()
    }
