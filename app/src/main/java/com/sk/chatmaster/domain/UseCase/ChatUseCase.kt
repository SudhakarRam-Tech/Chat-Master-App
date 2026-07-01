package com.sk.chatmaster.domain.UseCase

import com.sk.chatmaster.core.common.Resource
import com.sk.chatmaster.data.model.Message
import com.sk.chatmaster.domain.Repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

fun buildChatID(senderID : String, receiverID : String) : String {
    return if (senderID < receiverID) "${senderID}_${receiverID}" else "${receiverID}_${senderID}"
}
class GetChatUseCase @Inject constructor(private val repository: ChatRepository){
    operator fun invoke(senderID : String, receiverID: String) : Flow<Resource<List<Message>>> {
        val chatID = buildChatID(senderID,receiverID)
        return repository.getMessage(chatID)
    }
}
class SendTextMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        currentUid: String,
        otherUid: String,
        message: String
    ): Resource<Unit> {
        if (message.isBlank()) return Resource.Error("Message cannot be empty")
        val chatId = buildChatID(currentUid, otherUid)
        return repository.sendMessage(chatId, currentUid, otherUid, message.trim())
    }
}

class SendAudioMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        currentUid: String,
        otherUid: String,
        audioUrl: String,
        audioDuration: String
    ): Resource<Unit> {
        val chatId = buildChatID(currentUid, otherUid)
        return repository.sendAudioMessage(chatId, currentUid, otherUid, audioUrl, audioDuration)
    }
}

class MarkMessagesReadUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(currentUid: String, otherUid: String): Resource<Unit> {
        val chatId = buildChatID(currentUid, otherUid)
        return repository.markMessagesAsRead(chatId, currentUid)
    }
}