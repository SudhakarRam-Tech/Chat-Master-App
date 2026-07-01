package com.sk.chatmaster.domain.Repository

import com.sk.chatmaster.core.common.Resource
import com.sk.chatmaster.data.model.ChatUser

interface ChatListRepository {
    suspend fun getChatUserList(uid : String) : Resource<List<ChatUser>>

}