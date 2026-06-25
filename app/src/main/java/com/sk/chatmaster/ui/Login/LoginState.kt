package com.sk.chatmaster.ui.Login

data class LoginState(
    val name : String = "",
    val email : String = "",
    val password : String = "",
    val mobile : String = "",
    val isSignUp : Boolean = false,

    val error : String? = null,
    val success : Boolean = false,

    // API state

)
