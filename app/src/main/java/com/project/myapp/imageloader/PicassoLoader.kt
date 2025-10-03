package com.project.myapp.imageloader


import android.widget.ImageView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import javax.inject.Inject

class PicassoLoader @Inject constructor(): ImageLoader {
    override fun load(imageView: ImageView, url: String, placeholder: Int) {
        if (url.isEmpty()) return imageView.setImageResource(placeholder)
        Picasso
            .get()
            .load(url)
            .fit()
            .centerCrop()
            .placeholder(placeholder)
            .error(placeholder)
            .transform(CropCircleTransformation())
            .into(imageView)
    }
}
