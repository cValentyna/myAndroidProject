package com.project.myapp.ext.string

fun String.capitalizeFirstLetter(): String =
    this.replaceFirstChar {
        it.uppercaseChar()
    }
