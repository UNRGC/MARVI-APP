package com.marvilanundry.marvi.presentation.auth.recovery

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class RecoveryViewModel: ViewModel() {
    private val _state = MutableStateFlow(RecoveryState())
    val state: StateFlow<RecoveryState> = _state

    private fun isValidRecovery(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun onEmailChange(email: String) {
        _state.update { currentState ->
            val stateWithEmailChanged = currentState.copy(email = email)

            val forgotEnabled = isValidRecovery(
                stateWithEmailChanged.email
            )

            stateWithEmailChanged.copy(
                isRecoveryEnabled = forgotEnabled
            )
        }
    }
}

data class RecoveryState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isRecoveryEnabled: Boolean = false,
)