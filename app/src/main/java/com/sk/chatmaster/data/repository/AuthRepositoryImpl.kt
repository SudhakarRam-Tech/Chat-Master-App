package com.sk.chatmaster.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sk.chatmaster.core.common.AppConfig.Companion.CHAT_USERS_COLLECTION
import com.sk.chatmaster.core.common.Resource
import com.sk.chatmaster.data.model.UserInfo
import com.sk.chatmaster.domain.Repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Resource<String> {
        val loginResult : Resource<String> = try {
                val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()
                val uid = result.user?.uid
                if (!uid.isNullOrEmpty()) {
                    Resource.Success(uid)
                } else {
                    Resource.Error("User ID was empty", null)
                }
            } catch (e : Exception) {
                Resource.Error(e.localizedMessage ?: "Failed")
            }
        return loginResult
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        mobile: String
    ): Resource<UserInfo> {
        val registrationResult: Resource<UserInfo> = try {

            // 1. Create user in Firebase Auth sequentially
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid

            if (uid.isNullOrEmpty()) {
                Resource.Error("User ID was empty", null)
            } else {
                val user = hashMapOf(
                    "uid" to uid,
                    "name" to name,
                    "email" to email,
                    "password" to password,
                    "mobile" to mobile,
                    "createdAt" to Timestamp.now()
                )
                val userInfo = UserInfo(
                    uid = uid,
                    name = name,
                    password = password,
                    email = email,
                    mobile = mobile
                )

                // 2. Save user payload to Firestore sequentially
                firestore.collection(CHAT_USERS_COLLECTION)
                    .document(uid)
                    .set(user)
                    .await()

                // 3. Make this the absolute last expression of the else branch
                Resource.Success(userInfo)
            }
        } catch (e: Exception) {
            // 4. Handle any exceptions naturally here
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred",null)
        }

        // Explicitly return the final parsed type out of the function signature
        return registrationResult
    }
}