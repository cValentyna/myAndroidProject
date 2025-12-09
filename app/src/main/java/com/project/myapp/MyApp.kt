package com.project.myapp

import android.app.Application
import android.util.Log
import com.project.myapp.imageloader.ImageLibrary
import com.project.myapp.imageloader.ImageLibrarySelector
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(){
    @Inject lateinit var imageLibrarySelector: ImageLibrarySelector

    override fun onCreate() {
        super.onCreate()
        imageLibrarySelector.chosenLibrary = ImageLibrary.PICASSO
        Log.d("ImageDebug", "Application set chosenLibrary = ${imageLibrarySelector.chosenLibrary}")
    }
}
