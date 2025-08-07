package com.marvilanundry.marvi.presentation.auth.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvilanundry.marvi.domain.model.Client
import com.marvilanundry.marvi.domain.model.Order
import com.marvilanundry.marvi.domain.model.Orders
import com.marvilanundry.marvi.domain.model.Services
import com.marvilanundry.marvi.domain.model.UpdateClient
import com.marvilanundry.marvi.domain.usecase.GetOrderByIdUseCase
import com.marvilanundry.marvi.domain.usecase.GetOrdersByClientUseCase
import com.marvilanundry.marvi.domain.usecase.GetServicesUseCase
import com.marvilanundry.marvi.domain.usecase.PutUpdateClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val getOrdersByClientUseCase: GetOrdersByClientUseCase,
    private val getServicesUseCase: GetServicesUseCase,
    private val putUpdateClientUseCase: PutUpdateClientUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    fun validateClient(
        code: String = _state.value.clientCode,
        name: String = _state.value.clientName,
        firstSurname: String = _state.value.clientFirstSurname,
        phone: String = _state.value.clientPhone,
        email: String = _state.value.clientEmail,
        password: String = _state.value.clientPassword
    ): Boolean {
        return code.length >= 3 && name.length >= 3 && firstSurname.length >= 3 && phone.length >= 10 && email.length >= 3 && password.length >= 8
    }

    fun onOrderChange(order: String) {
        _state.update { currentState ->
            val stateWithOrderChanged = currentState.copy(orderInput = order, followedOrder = null)
            val order = order.toIntOrNull() ?: 0
            val isFollowEnabled = order > 0

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
            val toQuoteInput = toQuote.toIntOrNull() ?: 0
            val selectedService = currentState.services?.getOrNull(currentState.service)
            val total = if (selectedService != null && toQuoteInput > 0) {
                toQuoteInput * selectedService.precio
            } else {
                0.0
            }
            currentState.copy(
                toQuoteInput = toQuote, toQuote = String.format(Locale.getDefault(), "%.2f", total)
            )
        }
    }

    fun onClientCodeChange(code: String) {
        _state.update { currentState ->
            currentState.copy(clientCode = code, isSaveEnabled = validateClient(code = code))
        }
    }

    fun onClientNameChange(name: String) {
        _state.update { currentState ->
            currentState.copy(clientName = name, isSaveEnabled = validateClient(name = name))
        }
    }

    fun onClientFirstSurnameChange(surname: String) {
        _state.update { currentState ->
            currentState.copy(clientFirstSurname = surname, isSaveEnabled = validateClient(firstSurname = surname))
        }
    }

    fun onClientSecondSurnameChange(surname: String) {
        _state.update { currentState ->
            currentState.copy(clientSecondSurname = surname, isSaveEnabled = validateClient())
        }
    }

    fun onClientPhoneChange(phone: String) {
        _state.update { currentState ->
            currentState.copy(clientPhone = phone, isSaveEnabled = validateClient(phone = phone))
        }
    }

    fun onClientEmailChange(email: String) {
        _state.update { currentState ->
            currentState.copy(clientEmail = email, isSaveEnabled = validateClient(email = email))
        }
    }

    fun onClientPasswordChange(password: String) {
        _state.update { currentState ->
            currentState.copy(clientPassword = password, isSaveEnabled = validateClient(password = password))
        }
    }

    fun followOrder() {
        _state.value =
            _state.value.copy(isLoading = true, order = null, message = null, error = null)
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
        _state.value =
            _state.value.copy(isLoading = true, followedOrder = null, message = null, error = null)
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
        val client = _state.value.clientId ?: return
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
        _state.value = _state.value.copy(
            service = service, error = null, message = null
        )
    }

    fun setClient(client: Client?) {
        _state.update { currentState ->
            currentState.copy(
                clientId = client?.id_cliente,
                clientCode = client?.codigo ?: "",
                clientName = client?.nombre ?: "",
                clientFirstSurname = client?.primer_apellido ?: "",
                clientSecondSurname = client?.segundo_apellido ?: "",
                clientPhone = client?.telefono ?: "",
                clientEmail = client?.correo ?: "",
                clientPassword = client?.contrasena ?: "",
                client = client,
                message = null,
                error = null
            )
        }
        getOrders()
        getServices()
    }

    fun updateClient() {
        _state.value = _state.value.copy(isLoading = true, error = null, message = null)
        viewModelScope.launch {
            val updateClient = UpdateClient(
                id_cliente = _state.value.clientId ?: 0,
                codigo = _state.value.clientCode,
                nombre = _state.value.clientName,
                primer_apellido = _state.value.clientFirstSurname,
                segundo_apellido = _state.value.clientSecondSurname.ifBlank { null },
                telefono = _state.value.clientPhone,
                correo = _state.value.clientEmail,
                contrasena = _state.value.clientPassword
            )

            try {
                val response = putUpdateClientUseCase(updateClient)

                Log.d("HomeViewModel", "updateClient: $response")
                _state.value = _state.value.copy(
                    message = response, client = _state.value.client?.copy(
                        codigo = updateClient.codigo,
                        nombre = updateClient.nombre,
                        primer_apellido = updateClient.primer_apellido,
                        segundo_apellido = updateClient.segundo_apellido,
                        telefono = updateClient.telefono,
                        correo = updateClient.correo
                    ), isEditEnabled = false, isLoading = false
                )
            } catch (e: Exception) {
                Log.d("HomeViewModel", "updateClient: ${e.message}")
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun editEnabled(enabled: Boolean) {
        _state.value = _state.value.copy(isEditEnabled = enabled, clientPassword = "")
    }

    fun resetOrder() {
        _state.value = _state.value.copy(
            order = null, message = null, error = null
        )
    }

    fun resetClient() {
        _state.value = _state.value.copy(
            clientId = _state.value.client?.id_cliente,
            clientCode = _state.value.client?.codigo ?: "",
            clientName = _state.value.client?.nombre ?: "",
            clientFirstSurname = _state.value.client?.primer_apellido ?: "",
            clientSecondSurname = _state.value.client?.segundo_apellido ?: "",
            clientPhone = _state.value.client?.telefono ?: "",
            clientEmail = _state.value.client?.correo ?: "",
            clientPassword = _state.value.client?.contrasena ?: "",
            message = null,
            error = null
        )
    }
}

data class HomeUiState(
    val orderInput: String = "",
    val searchInput: String = "",
    val toQuoteInput: String = "",
    val toQuote: String = "0.00",
    val clientId: Int? = null,
    val clientCode: String = "",
    val clientName: String = "",
    val clientFirstSurname: String = "",
    val clientSecondSurname: String = "",
    val clientPhone: String = "",
    val clientEmail: String = "",
    val clientPassword: String = "",
    val client: Client? = null,
    val message: String? = null,
    val error: String? = null,
    val followedOrder: Order? = null,
    val order: Order? = null,
    val orders: List<Orders>? = null,
    val service: Int = 0,
    val services: List<Services>? = null,
    val isFollowEnabled: Boolean = false,
    val isEditEnabled: Boolean = false,
    val isSaveEnabled: Boolean = false,
    val isLoading: Boolean = false
)