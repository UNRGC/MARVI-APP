package com.marvilanundry.marvi.presentation.auth.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvilanundry.marvi.domain.usecase.GetApiWakeUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val getApiWakeUpUseCase: GetApiWakeUpUseCase
): ViewModel() {
    private val _state = MutableStateFlow(WelcomeUiState())
    val state: StateFlow<WelcomeUiState> = _state

    fun wakeUpApi() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val response = getApiWakeUpUseCase()

                _state.value = _state.value.copy(available = response, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    init {
        wakeUpApi()
    }
}

data class WelcomeUiState(
    val error: String? = null,
    val available: Boolean = false,
    val isLoading: Boolean = false
)