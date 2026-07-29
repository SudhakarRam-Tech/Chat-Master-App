package com.sk.chatmaster.ui.widget

sealed class DialogState {
    object Hidden : DialogState()
    data class Success(val successMsg : String,val destinationRoute: String? = null) : DialogState()
    data class Failure(val errorMsg : String) : DialogState()
}
