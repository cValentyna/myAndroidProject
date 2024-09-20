package com.project.myapp.screens.auth

// class where information about the state is stored
data class AuthState(
    var isUserEmailValid: Boolean = false,
    var isUserPasswordValid: Boolean = false,
    var wasFocusEmail: Boolean = false,
    var firstFocusEmail: Boolean = false,
    var wasFocusPassword: Boolean = false,
    var firstFocusPassword: Boolean = false,
    var wasRegisterButtonClicked: Boolean = false,
)
