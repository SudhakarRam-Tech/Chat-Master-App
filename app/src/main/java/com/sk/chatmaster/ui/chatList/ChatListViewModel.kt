package com.sk.chatmaster.ui.chatList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sk.chatmaster.core.common.Resource
import com.sk.chatmaster.data.model.ChatUser
import com.sk.chatmaster.data.model.UserInfo
import com.sk.chatmaster.domain.UseCase.GetChatUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChatListViewModel @Inject constructor(private val chatUsersUseCase: GetChatUsersUseCase): ViewModel() {
    val _chatListState  = MutableStateFlow<ChatUserListUiState>(ChatUserListUiState.Loading)
    val chatListState : StateFlow<ChatUserListUiState> = _chatListState.asStateFlow()

    fun loadChatUsers(uid : String) {
        viewModelScope.launch {
            _chatListState.value = ChatUserListUiState.Loading
            when (val result = chatUsersUseCase.invokeGetUsers(uid)) {//call start here
                is Resource.Success -> {
                    _chatListState.value = ChatUserListUiState.Success(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _chatListState.value =
                        ChatUserListUiState.Failure(result.message ?: "Failed to load chat users")
                }
                is Resource.Loading -> _chatListState.value = ChatUserListUiState.Loading
            }

        }
    }


}

sealed class ChatUserListUiState {
    object Loading : ChatUserListUiState()
    data class Success(val userList : List<ChatUser>) : ChatUserListUiState()
    data class Failure(val message : String) : ChatUserListUiState()
}