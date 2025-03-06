package com.project.myapp.ext.context

import android.content.Context
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.project.myapp.R

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.customAnimationForward(): ActivityOptionsCompat =
    ActivityOptionsCompat.makeCustomAnimation(
        this,
        R.anim.slide_in_left,
        R.anim.slide_out_left,
    )

fun Context.customAnimationBackward(): ActivityOptionsCompat =
    ActivityOptionsCompat.makeCustomAnimation(
        this,
        R.anim.slide_in_right,
        R.anim.slide_out_right,
    )
