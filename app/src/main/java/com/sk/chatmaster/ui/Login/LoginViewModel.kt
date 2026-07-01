package com.sk.chatmaster.ui.Login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sk.chatmaster.core.common.AppConfig
import com.sk.chatmaster.core.common.Resource
import com.sk.chatmaster.data.model.UserInfo
import com.sk.chatmaster.domain.UseCase.LoginUseCase
import com.sk.chatmaster.domain.UseCase.SignUpUseCase
import com.sk.chatmaster.navigation.Route
import com.sk.chatmaster.ui.widget.DialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase,
    private val signUpUseCase: SignUpUseCase) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    var loginState : StateFlow<LoginState> = _loginState.asStateFlow()
    private val _dialogState = MutableStateFlow<DialogState>(DialogState.Hidden)
    var dialogState : StateFlow<DialogState> = _dialogState.asStateFlow()

    fun loginEvent(event: AuthUIEvent) {
        when (event) {
            is AuthUIEvent.EmailChanged -> _loginState.update { state ->
                state.copy(email = event.value)
            }
            is AuthUIEvent.PassworkdChaned -> _loginState.update {
                state -> state.copy(password = event.value)
            }
            is AuthUIEvent.SetSignUp -> _loginState.update { state ->
                state.copy(isSignUp = event.value)
            }
            is AuthUIEvent.submitOnClick -> loginSubmit()

            else -> {
                //nothing
            }
        }
    }
    fun registerEvent(event : AuthUIEvent) {
        when(event) {
            is AuthUIEvent.NameChanged -> _loginState.update { state ->
                state.copy(name = event.value)
            }
            is AuthUIEvent.EmailChanged -> _loginState.update { state ->
                state.copy(email = event.value)
            }
            is AuthUIEvent.PassworkdChaned -> _loginState.update { state ->
                state.copy(password = event.value)
            }
            is AuthUIEvent.MobileChanged -> _loginState.update { state ->
                state.copy(mobile = event.value)
            }
            is AuthUIEvent.submitOnClick -> signUpSubmit()
            is AuthUIEvent.SetSignUp -> _loginState.update { state ->
                state.copy(isSignUp = event.value)
            }
        }
    }

    private fun validateLoginMethod(): Boolean {
        var loginState = _loginState.value
        var errorMsg : String? = null
        when {
            loginState.email.isNullOrEmpty() -> errorMsg = "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(loginState.email).matches() -> errorMsg = "Invalid Email Address"

            loginState.password.isNullOrEmpty() -> errorMsg = "Password is required"
            loginState.password.length < 5 -> errorMsg = "Invalid password"

            else -> {
                errorMsg = null
            }
        }
        _loginState.update { state ->
            state.copy(error = errorMsg)
        }
        if (errorMsg.isNullOrEmpty()) {
            return true
        } else {
            return false
        }
    }
    private fun validateSignUpMethod(): Boolean {
        var loginState = _loginState.value
        var errorMsg : String? = null
        when {
            loginState.name.isNullOrEmpty() -> errorMsg = "Name is required"
            loginState.name.length < 2 -> errorMsg =  "Invalid Name"

            loginState.email.isNullOrEmpty() -> errorMsg = "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(loginState.email).matches() -> errorMsg = "Invalid Email Address"

            loginState.password.isNullOrEmpty() -> errorMsg = "Password is required"
            loginState.password.length < 5 -> errorMsg = "Invalid password"

            loginState.mobile.isNullOrBlank() -> errorMsg = "Mobile Number is required"
            loginState.mobile.length < 10 -> errorMsg = "Invalid Mobile Number"

            else -> {
                errorMsg = null
            }
        }
        _loginState.update { state ->
            state.copy(error = errorMsg)
        }
        if (errorMsg.isNullOrEmpty()) {
            return true
        } else {
            return false
        }
    }


    private fun loginSubmit() {
        if (validateLoginMethod()) {
            val state = _loginState.value
            _loginState.update { it.copy(isLoading = true, apiError = null) }
            val dialogState = dialogState.value
            viewModelScope.launch {
                val result : Resource<String> = loginUseCase.invokeLogin(state.email!!,state.password!!)
                when(result) {
                    is Resource.Success -> {
                        _loginState.update { state ->
                            state.copy(successMessage = "Login Successfully", isLoading = false)
                        }
                        _dialogState.value = DialogState.Success("Login Successfully", Route.ChatListScreen )
                        AppConfig.UID = result.data!!
                    }
                    is Resource.Error -> {
                        _loginState.update { state ->
                            AppConfig.UID = ""
                            state.copy(apiError = result.message, isLoading = false)
                        }
                        _dialogState.value = DialogState.Failure(result.message ?: "Failed")
                    }
                    is Resource.Loading -> {

                    }
                }
            }
        }
    }

    private fun signUpSubmit() {
        if (validateSignUpMethod()) {
            val state = _loginState.value
            viewModelScope.launch {
                val result : Resource<UserInfo> = signUpUseCase.invokeSignUp(name = state.name!!,
                        email = state.email!!,
                        password = state.password!!,
                        mobile = state.mobile!!)
                    when(result) {
                    is Resource.Success -> {
                        _loginState.update { state ->
                            AppConfig.UID = result.data!!.uid
                            state.copy(successMessage = "Sign-Up Successfully", isLoading = false)
                        }
                        _dialogState.value = DialogState.Success("Sign-Up Successfully", Route.ChatListScreen )
                    }
                    is Resource.Error -> {
                        _loginState.update { state ->
                            AppConfig.UID = ""
                            state.copy(apiError = result.message, isLoading = false)
                        }
                        _dialogState.value = DialogState.Failure(result.message ?: "Failed")
                    }
                    is Resource.Loading -> {

                    }
                }
            }

        }
    }

    fun clearError() {
        _loginState.update { state -> state.copy(error = null) }
    }
    fun dismissDialog() {
        _dialogState.value = DialogState.Hidden
    }

}
sealed class AuthUIEvent {
    data class NameChanged(val value : String) : AuthUIEvent()
    data class EmailChanged(val value : String) : AuthUIEvent()
    data class PassworkdChaned(val value : String) : AuthUIEvent()
    data class MobileChanged(val value : String) : AuthUIEvent()
    data class SetSignUp(val value : Boolean) : AuthUIEvent()
    object submitOnClick : AuthUIEvent()
    //object signupOnclick : AuthUIEvent()
}
