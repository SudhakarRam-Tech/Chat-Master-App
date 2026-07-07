package com.sk.chatmaster.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sk.chatmaster.core.common.AppConfig.Companion.CHATS
import com.sk.chatmaster.core.common.AppConfig.Companion.IS_READ
import com.sk.chatmaster.core.common.AppConfig.Companion.MESSAGES
import com.sk.chatmaster.core.common.AppConfig.Companion.RECEIVER_ID
import com.sk.chatmaster.core.common.AppConfig.Companion.TIMESTAMP
import com.sk.chatmaster.core.common.Resource
import com.sk.chatmaster.data.model.Message
import com.sk.chatmaster.data.model.MessageType
import com.sk.chatmaster.domain.Repository.ChatRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Real-time Firestore listener via callbackFlow.
 * Emits Resource.Loading once, then Resource.Success on every update.
 *
 * Firestore path: chats/{chatId}/messages  ordered by timestamp ASC
 */
class ChatRepositoryImpl @Inject constructor( private val firestore: FirebaseFirestore) : ChatRepository {
    override fun getMessage(chatId: String): Flow<Resource<List<Message>>> = callbackFlow  {
        trySend(Resource.Loading())

        val listener = firestore
            .collection(CHATS)
            .document(chatId)
            .collection(MESSAGES)
            .orderBy(TIMESTAMP, Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Failed to load messages"))
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Message::class.java)?.copy(messageId = doc.id)
                } ?: emptyList()
                trySend(Resource.Success(messages))
            }

        awaitClose { listener.remove() }  // cancel Firestore listener when Flow is cancelled
    }

    override suspend fun sendMessage(
        chatID: String,
        senderID: String,
        receiverID: String,
        message: String
    ): Resource<Unit> {
        return try {
            val msgRef = firestore
                .collection(CHATS)
                .document(chatID)
                .collection(MESSAGES)
                .document()
            val msg = Message(
                messageId   = msgRef.id,
                senderId    = senderID,
                receiverId  = receiverID,
                message     = message,
                messageType = MessageType.TEXT,
                timestamp   = Timestamp.now(),
                read      = false
            )

            msgRef.set(msg).await()
            Resource.Success(Unit)
        } catch (e : Exception) {
            Resource.Error(e.message ?: "Failed to send message")
        }
    }

    override suspend fun sendAudioMessage(
        chatId: String,
        senderId: String,
        receiverId: String,
        audioUrl: String,
        audioDuration: String
    ): Resource<Unit> {
        return try {
            val msgRef = firestore.collection(CHATS)
                .document(chatId)
                .collection(MESSAGES)
                .document()
            val msg = Message(
                messageId     = msgRef.id,
                senderId      = senderId,
                receiverId    = receiverId,
                message       = "",
                messageType   = MessageType.AUDIO,
                audioUrl      = audioUrl,
                audioDuration = audioDuration,
                timestamp     = Timestamp.now(),
                read        = false
            )
            msgRef.set(msg).await()
            Resource.Success(Unit)
        } catch (e : Exception) {
            Resource.Error(e.message ?: "Failed to send audio message")
        }
    }

    override suspend fun markMessagesAsRead(
        chatId: String,
        currentUserId: String
    ): Resource<Unit> {
        return try {
            val unread = firestore.collection(CHATS)
                .document(chatId)
                .collection(MESSAGES)
                .whereEqualTo(RECEIVER_ID,currentUserId)
                .whereEqualTo(IS_READ,false)
                .get()
                .await()

            val batch = firestore.batch()
            unread.documents.forEach { doc ->
                batch.update(doc.reference, IS_READ, true)
            }
            batch.commit().await()
            Resource.Success(Unit)
        } catch (e : Exception) {
            Resource.Error(e.message ?: "Failed to mark messages as read")
        }
    }
}