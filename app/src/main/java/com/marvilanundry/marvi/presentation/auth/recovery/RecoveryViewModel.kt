package com.marvilanundry.marvi.presentation.auth.recovery

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvilanundry.marvi.domain.model.ResetPassword
import com.marvilanundry.marvi.domain.usecase.PostForgotPasswordUseCase
import com.marvilanundry.marvi.domain.usecase.PostResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class RecoveryStep {
    ENTER_EMAIL,
    ENTER_CODE_AND_PASSWORD
}

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val postForgotPasswordUseCase: PostForgotPasswordUseCase,
    private val postResetPasswordUseCase: PostResetPasswordUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RecoveryUiState())
    val state: StateFlow<RecoveryUiState> = _state

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun calculateProgress(updatedState: RecoveryUiState): Float {
        return if (updatedState.step == RecoveryStep.ENTER_EMAIL) {
            if (isValidEmail(updatedState.email)) 0.5f else 0f
        } else {
            val fields = listOf(updatedState.code, updatedState.password, updatedState.confirmPassword)
            0.5f + (fields.count { it.isNotBlank() } / 3f) * 0.5f
        }
    }

    fun onEmailChange(email: String) {
        _state.update { currentState ->
            val stateWithEmailChanged = currentState.copy(email = email)
            stateWithEmailChanged.copy(
                isRecoveryEnabled = isValidEmail(email.replace(" ", "")),
                progressBar = calculateProgress(stateWithEmailChanged)
            )
        }
    }

    fun onCodeChange(code: String) {
        _state.update { it.copy(code = code) }
        validateResetFields()
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
        validateResetFields()
    }

    fun onConfirmPasswordChange(password: String) {
        _state.update { it.copy(confirmPassword = password) }
        validateResetFields()
    }

    private fun validateResetFields() {
        _state.update { currentState ->
            val isEnabled = currentState.code.isNotBlank() &&
                    currentState.password.isNotBlank() &&
                    currentState.password == currentState.confirmPassword &&
                    currentState.password.length >= 6

            currentState.copy(
                isResetEnabled = isEnabled,
                progressBar = calculateProgress(currentState)
            )
        }
    }

    fun sendOtp() {
        _state.update { it.copy(isLoading = true, message = null, error = null) }
        viewModelScope.launch {
            try {
                val response = postForgotPasswordUseCase(_state.value.email)
                _state.update { it.copy(
                    message = response,
                    isLoading = false,
                    step = RecoveryStep.ENTER_CODE_AND_PASSWORD
                ) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun resetPassword() {
        _state.update { it.copy(isLoading = true, message = null, error = null) }
        viewModelScope.launch {
            try {
                val resetData = ResetPassword(
                    codigo = _state.value.code,
                    contrasena = _state.value.password
                )
                val response = postResetPasswordUseCase(_state.value.email, resetData)
                _state.update { it.copy(message = response, isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun clearMessages() {
        _state.update { it.copy(message = null, error = null) }
    }
}

data class RecoveryUiState(
    val step: RecoveryStep = RecoveryStep.ENTER_EMAIL,
    val progressBar: Float = 0f,
    val email: String = "",
    val code: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val message: String? = null,
    val error: String? = null,
    val isRecoveryEnabled: Boolean = false,
    val isResetEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)