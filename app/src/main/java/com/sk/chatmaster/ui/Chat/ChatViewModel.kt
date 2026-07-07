package com.sk.chatmaster.ui.Chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sk.chatmaster.core.common.Resource
import com.sk.chatmaster.domain.UseCase.GetChatUseCase
import com.sk.chatmaster.domain.UseCase.MarkMessagesReadUseCase
import com.sk.chatmaster.domain.UseCase.SendAudioMessageUseCase
import com.sk.chatmaster.domain.UseCase.SendTextMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val getChatUseCase: GetChatUseCase,
                                private val sendTextMessageUseCase: SendTextMessageUseCase,
                                private val sendAudioMessageUseCase: SendAudioMessageUseCase,
                                private val markMessagesReadUseCase: MarkMessagesReadUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _sendState = MutableStateFlow<Resource<Unit>?>(null)
    val sendState: StateFlow<Resource<Unit>?> = _sendState.asStateFlow()

    private var currentUid: String = ""
    private var otherUid: String = ""

    fun init(currentUid: String, otherUid: String) {
        this.currentUid = currentUid
        this.otherUid   = otherUid
        observeMessages()
        markAsRead()
    }

    private fun observeMessages() {
        getChatUseCase(currentUid, otherUid)
            .onEach { result ->
                _uiState.value = when (result) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(result.data ?: emptyList())
                    is Resource.Error   -> UiState.Failure(result.message ?: "Error loading messages")
                }
            }
            .launchIn(viewModelScope)
        /*viewModelScope.launch {
            val result = getChatUseCase.invoke(senderID = currentUid, receiverID = otherUid)
            when(result) {
                is Resource.Loading<*> -> UiState.Loading
                is Resource.Success -> UiState.Success(result.)
            }

        }*/
    }

    fun sendTextMessage(message: String) {
        viewModelScope.launch {
            _sendState.value = Resource.Loading()
            _sendState.value = sendTextMessageUseCase(currentUid, otherUid, message)
        }
    }

    fun sendAudioMessage(audioUrl: String, duration: String) {
        viewModelScope.launch {
            _sendState.value = Resource.Loading()
            _sendState.value = sendAudioMessageUseCase(currentUid, otherUid, audioUrl, duration)
        }
    }

    private fun markAsRead() {
        viewModelScope.launch {
            when (val result = markMessagesReadUseCase.invoke(currentUid, otherUid)) {
                is Resource.Error -> Log.e("Chat", "Mark read failed: ${result.message}")
                else -> {}
            }

        }
    }

    fun resetSendState() {
        _sendState.value = null
    }

}