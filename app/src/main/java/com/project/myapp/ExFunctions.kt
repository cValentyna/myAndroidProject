package com.project.myapp

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

object ExtensionUtils {
    fun AppCompatActivity.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
