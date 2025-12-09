package com.project.myapp.di

import android.util.Log
import com.project.myapp.imageloader.ImageLibrarySelector
import com.project.myapp.imageloader.ImageLoader
import com.project.myapp.imageloader.createLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {
    @Provides
    @Singleton
    fun provideImageLoader(imageLibrarySelector: ImageLibrarySelector): ImageLoader {
        val lib = imageLibrarySelector.chosenLibrary
        Log.d("ImageDebug", "provideImageLoader: creating loader for $lib")
        return lib.createLoader()
    }
}
