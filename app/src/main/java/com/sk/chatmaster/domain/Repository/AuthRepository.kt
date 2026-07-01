package com.sk.chatmaster.domain.Repository

import com.sk.chatmaster.core.common.Resource
import com.sk.chatmaster.data.model.UserInfo

interface AuthRepository {
    suspend fun login(email : String,password : String) : Resource<String>
    suspend fun register(name : String, email: String, password: String, mobile : String) : Resource<UserInfo>

}