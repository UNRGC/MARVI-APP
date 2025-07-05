package com.marvilanundry.marvi.presentation.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvilanundry.marvi.domain.model.Client
import com.marvilanundry.marvi.domain.model.Login
import com.marvilanundry.marvi.domain.usecase.PostLoginClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLoginUseCase: PostLoginClientUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

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

    fun login() {
        _state.value = _state.value.copy(isLoading = true, client = null, error = null)
        viewModelScope.launch {
            val credentials = Login(
                correo = _state.value.email, contrasena = _state.value.password
            )
            try {
                val response = postLoginUseCase(credentials)
                _state.value = _state.value.copy(client = response, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val client: Client? = null,
    val error: String? = null,
    val isLoginEnabled: Boolean = false,
    val isLoading: Boolean = false
)