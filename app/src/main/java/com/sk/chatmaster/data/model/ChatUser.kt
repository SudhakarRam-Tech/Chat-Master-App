package com.sk.chatmaster.data.model

data class ChatUser(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val mobile: String = "",
    val password: String = "",
    val lastLogin: String? = null,
    val lastMsg: String? = null,
    var icon: String? = null
)
