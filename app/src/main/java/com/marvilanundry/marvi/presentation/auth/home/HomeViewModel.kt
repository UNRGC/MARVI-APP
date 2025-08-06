package com.marvilanundry.marvi.presentation.auth.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvilanundry.marvi.domain.model.Order
import com.marvilanundry.marvi.domain.usecase.GetOrderByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getOrderByIdUseCase: GetOrderByIdUseCase
): ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    fun onOrderChange(order: String) {
        _state.update { currentState ->
            val stateWithOrderChanged = currentState.copy(orderInput = order, order = null)

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

    fun followOrder() {
        _state.value = _state.value.copy(isLoading = true, message = null, error = null)
        viewModelScope.launch {
            try {
                val response = getOrderByIdUseCase(_state.value.orderInput.toInt())
                _state.value = _state.value.copy(order = response, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}

data class HomeUiState(
    val orderInput: String = "",
    val searchInput: String = "",
    val toQuoteInput: String = "",
    val message: String? = null,
    val error: String? = null,
    val order: Order? = null,
    val isFollowEnabled: Boolean = false,
    val isCalculationEnabled: Boolean = false,
    val isLoading: Boolean = false
)