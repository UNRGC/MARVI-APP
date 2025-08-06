package com.marvilanundry.marvi.presentation.auth.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvilanundry.marvi.domain.model.Client
import com.marvilanundry.marvi.domain.model.Order
import com.marvilanundry.marvi.domain.model.Orders
import com.marvilanundry.marvi.domain.model.Services
import com.marvilanundry.marvi.domain.usecase.GetOrderByIdUseCase
import com.marvilanundry.marvi.domain.usecase.GetOrdersByClientUseCase
import com.marvilanundry.marvi.domain.usecase.GetServicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val getOrdersByClientUseCase: GetOrdersByClientUseCase,
    private val getServicesUseCase: GetServicesUseCase
): ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    fun onOrderChange(order: String) {
        _state.update { currentState ->
            val stateWithOrderChanged = currentState.copy(orderInput = order, followedOrder = null)

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
        if (search.isBlank()) getOrders()
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
                _state.value = _state.value.copy(followedOrder = response, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun getOrder(order: Int) {
        _state.value = _state.value.copy(isLoading = true, message = null, error = null)
        viewModelScope.launch {
            try {
                val response = getOrderByIdUseCase(order)
                _state.value = _state.value.copy(order = response, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun getOrders() {
        val client = _state.value.client?.id_cliente ?: return
        val search = _state.value.searchInput.ifBlank { null }

        _state.value = _state.value.copy(isLoading = true, message = null, error = null)
        viewModelScope.launch {
            try {
                val response = getOrdersByClientUseCase(clientId = client, search = search)
                _state.value = _state.value.copy(orders = response, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun getServices() {
        _state.value = _state.value.copy(isLoading = true, message = null, error = null)
        viewModelScope.launch {
            try {
                val response = getServicesUseCase()
                _state.value = _state.value.copy(services = response, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun setService(service: Int) {
        _state.value = _state.value.copy(service = service, toQuoteInput = "", toQuote = "0.00", error = null, message = null)
    }

    fun calculateService() {
        val toQuoteInput = _state.value.toQuoteInput.toIntOrNull() ?: return
        val toQuoteServices = _state.value.service?.let { _state.value.services?.get(it) }?.precio ?: return
        val toQuote = toQuoteInput * toQuoteServices
        if (toQuoteInput <= 0) {
            _state.value = _state.value.copy(error = "La cantidad debe ser mayor a 0")
            return
        }
        _state.value = _state.value.copy(toQuote = toQuote.toString(), error = null, message = null)
    }

    fun setClient(client: Client?) {
        _state.update { currentState ->
            currentState.copy(client = client, message = null, error = null)
        }
        getOrders()
        getServices()
    }

    fun resetState() {
        _state.value = HomeUiState().copy(searchInput = _state.value.searchInput, client = _state.value.client, orders = _state.value.orders)
    }
}

data class HomeUiState(
    val orderInput: String = "",
    val searchInput: String = "",
    val toQuoteInput: String = "",
    val toQuote: String = "0.00",
    val message: String? = null,
    val error: String? = null,
    val client: Client? = null,
    val followedOrder: Order? = null,
    val order: Order? = null,
    val orders: List<Orders>? = null,
    val service: Int? = null,
    val services: List<Services>? = null,
    val isFollowEnabled: Boolean = false,
    val isCalculationEnabled: Boolean = false,
    val isLoading: Boolean = false
)