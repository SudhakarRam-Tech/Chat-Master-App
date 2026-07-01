package com.sk.chatmaster.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.sk.chatmaster.data.repository.AuthRepositoryImpl
import com.sk.chatmaster.data.repository.ChatRepositoryImpl
import com.sk.chatmaster.data.repository.ChatUserListRepositoryImpl
import com.sk.chatmaster.domain.Repository.AuthRepository
import com.sk.chatmaster.domain.Repository.ChatListRepository
import com.sk.chatmaster.domain.Repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFireBaseAuth() : FirebaseAuth  = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFireBaseFirestore() : FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFireBaseDatabase() : FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun bindAuthRepository(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore) : AuthRepository {
        return AuthRepositoryImpl(firebaseAuth,firebaseFirestore)
    }

    @Provides
    @Singleton
    fun providesChatUserListRepository(firestore: FirebaseFirestore) : ChatListRepository {
        return ChatUserListRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideChatRepository(firestore: FirebaseFirestore) : ChatRepository {
        return ChatRepositoryImpl(firestore)
    }
}