package com.project.myapp.ext.componentactivity

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge

fun ComponentActivity.handleBackPress() {
    onBackPressedDispatcher.addCallback(this) {
        moveTaskToBack(true)
    }
}

fun ComponentActivity.setEnableEdgeToEdge() {
    enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.GRAY))
}

