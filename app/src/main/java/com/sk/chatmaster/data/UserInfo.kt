package com.sk.chatmaster.data

data class UserInfo(
    val uid : String,
    val name : String? = "",
    val email : String? = "",
    val password : String? = "",
    val mobile : String? = ""
)
