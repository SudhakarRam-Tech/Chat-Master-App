package com.sk.chatmaster.domain.Repository

import com.sk.chatmaster.core.common.Resource
import com.sk.chatmaster.data.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessage(chatId : String) : Flow<Resource<List<Message>>>
    suspend fun sendMessage(chatID : String,
                            senderID : String,
                            receiverID : String,
                            message: String,
                            ): Resource<Unit>
    suspend fun sendAudioMessage(
        chatId: String,
        senderId: String,
        receiverId: String,
        audioUrl: String,
        audioDuration: String
    ): Resource<Unit>

    suspend fun markMessagesAsRead(chatId: String, currentUserId: String): Resource<Unit>
}