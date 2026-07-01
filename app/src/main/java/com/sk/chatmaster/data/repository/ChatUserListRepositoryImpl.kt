package com.sk.chatmaster.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sk.chatmaster.core.common.AppConfig.Companion.CHAT_USERS_COLLECTION
import com.sk.chatmaster.core.common.Resource
import com.sk.chatmaster.data.model.ChatUser
import com.sk.chatmaster.domain.Repository.ChatListRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatUserListRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) : ChatListRepository{
    override suspend fun getChatUserList(uid: String): Resource<List<ChatUser>> {
        return try {
            val snapshot = firestore
                .collection(CHAT_USERS_COLLECTION)
                .get()
                .await()

            val users = snapshot.documents
                .mapNotNull { doc ->
                    doc.toObject(ChatUser::class.java)?.copy(uid = doc.id)
                }
                .filter { it.uid != uid } // exclude self from the list

            Resource.Success<List<ChatUser>>(users)
        } catch (e: Exception) {
            Resource.Error<List<ChatUser>>(e.message ?: "Failed to load chat users", null)
        }

    }

}