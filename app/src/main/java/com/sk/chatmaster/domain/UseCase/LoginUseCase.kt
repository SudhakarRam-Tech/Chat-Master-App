package com.sk.chatmaster.domain.UseCase

import com.sk.chatmaster.core.common.Resource
import com.sk.chatmaster.domain.Repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository)  {
    suspend fun invokeLogin(email : String, password : String) : Resource<String> {
        return repository.login(email,password)
    }
}