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

    private fun isValidLogin(
        email: String = _state.value.email, password: String = _state.value.password
    ): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 8
    }

    fun onEmailChange(email: String) {
        _state.update { currentState ->
            currentState.copy(email = email, isLoginEnabled = isValidLogin(email = email))
        }
    }

    fun onPasswordChange(password: String) {
        _state.update { currentState ->
            currentState.copy(
                password = password, isLoginEnabled = isValidLogin(password = password)
            )
        }
    }

    fun login() {
        _state.value = _state.value.copy(isLoading = true, error = null)
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

    fun resetState() {
        _state.value = LoginUiState()
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