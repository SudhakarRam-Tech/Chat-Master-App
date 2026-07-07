package com.sk.chatmaster.ui.Login

data class LoginState(
    val name : String? = "",
    val email : String? = "",
    val password : String? = "",
    val confirmPassword : String? = "",
    val mobile : String? = "",
    val isSignUp : Boolean = false,
    val uid : String? = null,

    val error : String? = null,
    val success : Boolean = false,

    // API state
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val apiError: String? = null
)
