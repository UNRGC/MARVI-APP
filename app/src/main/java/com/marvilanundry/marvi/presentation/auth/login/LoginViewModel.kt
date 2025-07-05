package com.marvilanundry.marvi.presentation.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private fun isValidLogin(email: String, password: String): Boolean {
        val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isValidPassword = password.length >= 8
        return isValidEmail && isValidPassword
    }

    fun onEmailChange(email: String) {
        _state.update { currentState ->
            val stateWithEmailChanged = currentState.copy(email = email)

            val registerEnabled = isValidLogin(
                stateWithEmailChanged.email,
                stateWithEmailChanged.password,
            )

            stateWithEmailChanged.copy(
                isLoginEnabled = registerEnabled
            )
        }
    }

    fun onPasswordChange(password: String) {
        _state.update { currentState ->
            val stateWithPasswordChanged = currentState.copy(password = password)

            val registerEnabled = isValidLogin(
                stateWithPasswordChanged.email,
                stateWithPasswordChanged.password,
            )

            stateWithPasswordChanged.copy(
                isLoginEnabled = registerEnabled
            )
        }
    }
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginEnabled: Boolean = false,
)