package com.project.myapp

import android.app.Application

class MyApp : Application() {
    val dataStore: DataStore by lazy {
        DataStore(this)
    }
}
