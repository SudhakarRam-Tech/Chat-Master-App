package com.sk.chatmaster.ui.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    var loginState : StateFlow<LoginState> = _loginState.asStateFlow()

    fun login() {
        viewModelScope.launch {

        }
    }

    fun register() {
        viewModelScope.launch {

        }
    }
 }