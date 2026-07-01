package com.sk.chatmaster.domain.UseCase

import com.sk.chatmaster.core.common.Resource
import com.sk.chatmaster.data.model.UserInfo
import com.sk.chatmaster.domain.Repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend fun invokeSignUp(name : String,email: String,password: String,mobile : String) : Resource<UserInfo> {
        return repository.register(name,email,password,mobile)
    }
}