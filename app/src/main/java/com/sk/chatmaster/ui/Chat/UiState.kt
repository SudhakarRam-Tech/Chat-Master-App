package com.sk.chatmaster.ui.Chat

import com.sk.chatmaster.data.model.Message

sealed class UiState {
    object Loading : UiState()
    data class Success(val message : List<Message>) : UiState()
    data class Failure(val message : String) : UiState()
}