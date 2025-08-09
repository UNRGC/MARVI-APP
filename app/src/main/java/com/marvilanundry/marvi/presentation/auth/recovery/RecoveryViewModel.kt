package com.marvilanundry.marvi.presentation.auth.recovery

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvilanundry.marvi.domain.model.Email
import com.marvilanundry.marvi.domain.usecase.PostResetPasswordClientUseCase
import com.marvilanundry.marvi.presentation.auth.login.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.isNotBlank

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val postResetPasswordClientUseCase: PostResetPasswordClientUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RecoveryUiState())
    val state: StateFlow<RecoveryUiState> = _state

    private fun isValidRecovery(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun calculateProgress(updatedState: RecoveryUiState): Float {
        return listOf(
            updatedState.email
        ).count { it.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(it).matches() } / 1f
    }

    fun onEmailChange(email: String) {
        _state.update { currentState ->
            val stateWithEmailChanged = currentState.copy(email = email)
            val progress = calculateProgress(stateWithEmailChanged)

            stateWithEmailChanged.copy(
                isRecoveryEnabled = isValidRecovery(email.replace(" ", "")), progressBar = progress
            )
        }
    }

    fun recovery() {
        _state.value = _state.value.copy(isLoading = true, message = null, error = null)
        viewModelScope.launch {
            val email = Email(_state.value.email)

            try {
                val response = postResetPasswordClientUseCase(email)
                _state.value = _state.value.copy(message = response, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}

data class RecoveryUiState(
    val progressBar: Float = 0f,
    val email: String = "",
    val message: String? = null,
    val error: String? = null,
    val isRecoveryEnabled: Boolean = false,
    val isLoading: Boolean = false
)