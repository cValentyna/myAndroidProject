package com.project.myapp.screens.splashScreen

sealed interface GetCachedCredentials {
    data object Initial : GetCachedCredentials

    data class Success(val savedName: String) : GetCachedCredentials

    data object Fail : GetCachedCredentials
}
