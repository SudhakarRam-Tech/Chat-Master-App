package com.sk.chatmaster.domain.UseCase

import com.sk.chatmaster.core.common.Resource
import com.sk.chatmaster.data.model.ChatUser
import com.sk.chatmaster.domain.Repository.ChatListRepository
import javax.inject.Inject

class GetChatUsersUseCase @Inject constructor(private val chatListRepository: ChatListRepository) {
    suspend fun invokeGetUsers(uid : String) : Resource<List<ChatUser>> {
        return chatListRepository.getChatUserList(uid)
    }

}