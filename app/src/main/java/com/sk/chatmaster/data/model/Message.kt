package com.sk.chatmaster.data.model

import com.google.firebase.Timestamp

/**
 * Firestore collection: chats/{chatId}/messages/{messageId}
 *
 * Sample document:
 * {
 *   "messageId"   : "msg_abc123",
 *   "senderId"    : "uid_sender",
 *   "receiverId"  : "uid_receiver",
 *   "message"     : "Hey! How are you?",
 *   "messageType" : "text",          // "text" | "audio"
 *   "audioUrl"    : null,            // Firebase Storage URL for audio messages
 *   "audioDuration": null,           // duration string e.g. "02:30"
 *   "timestamp"   : Timestamp,
 *   "isRead"      : false
 * }
 */
data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val messageType: String = MessageType.TEXT,  // "text" or "audio"
    val audioUrl: String? = null,
    val audioDuration: String? = null,
    val timestamp: Timestamp? = null,
    val read: Boolean = false
)

object MessageType {
    const val TEXT  = "text"
    const val AUDIO = "audio"
}