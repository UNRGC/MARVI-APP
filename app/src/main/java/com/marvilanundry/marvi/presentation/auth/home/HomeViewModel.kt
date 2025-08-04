package com.marvilanundry.marvi.presentation.auth.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel: ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    fun onOrderChange(order: String) {
        _state.update { currentState ->
            val stateWithOrderChanged = currentState.copy(orderInput = order)

            val isFollowEnabled = order.isNotBlank()

            stateWithOrderChanged.copy(
                isFollowEnabled = isFollowEnabled
            )
        }
    }
    fun onSearchChange(search: String) {
        _state.update { currentState ->
            currentState.copy(searchInput = search)
        }
    }
    fun onToQuoteChange(toQuote: String) {
        _state.update { currentState ->
            val stateWithToQuoteChanged = currentState.copy(toQuoteInput = toQuote)

            val isCalculationEnabled = toQuote.isNotBlank()

            stateWithToQuoteChanged.copy(
                isCalculationEnabled = isCalculationEnabled
            )
        }
    }
}

data class HomeUiState(
    val orderInput: String = "",
    val searchInput: String = "",
    val toQuoteInput: String = "",
    val message: String? = null,
    val error: String? = null,
    val isFollowEnabled: Boolean = false,
    val isCalculationEnabled: Boolean = false,
    val isLoading: Boolean = false
)