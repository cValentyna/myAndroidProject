package com.project.myapp.ext.view

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

fun View.initializeWindowInsetsHandling() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets
    }
}

fun View.snackBar(
    message: String,
    actionText: String,
    duration: Int = Snackbar.LENGTH_LONG,
    onDismiss: () -> Unit = {},
    action: () -> Unit,
) {
    Snackbar
        .make(this, message, Snackbar.LENGTH_INDEFINITE)
        .setDuration(duration)
        .setAction(actionText) { action() }
        .addCallback(object:Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if(event != DISMISS_EVENT_ACTION){
                    onDismiss()
                }
            }
        })
        .show()
}
