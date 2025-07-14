package com.project.myapp.screens.splashScreen

sealed interface CredentialsRetrievalState {
    data object Initial : CredentialsRetrievalState

    data class Success(val savedName: String) : CredentialsRetrievalState

    data object Fail : CredentialsRetrievalState
}
