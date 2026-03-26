package com.marvilanundry.marvi.presentation.auth.home

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvilanundry.marvi.domain.model.ChangePassword
import com.marvilanundry.marvi.domain.model.Client
import com.marvilanundry.marvi.domain.model.Order
import com.marvilanundry.marvi.domain.model.Orders
import com.marvilanundry.marvi.domain.model.Services
import com.marvilanundry.marvi.domain.model.UpdateClient
import com.marvilanundry.marvi.domain.usecase.GetOrderByIdUseCase
import com.marvilanundry.marvi.domain.usecase.GetOrdersByClientUseCase
import com.marvilanundry.marvi.domain.usecase.GetServicesUseCase
import com.marvilanundry.marvi.domain.usecase.PatchChangePasswordClientUseCase
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
    private val putUpdateClientUseCase: PutUpdateClientUseCase,
    private val patchChangePasswordClientUseCase: PatchChangePasswordClientUseCase
) : ViewModel() {
    private val orderDigitsRegex = Regex("^\\d+$")

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    private fun requireAuthToken(): String? {
        val token = _state.value.authToken
        if (token.isNullOrBlank()) {
            _state.value = _state.value.copy(error = "Token no proporcionado", isLoading = false)
            return null
        }
        return token
    }

    fun validateClient(
        name: String = _state.value.clientName,
        firstSurname: String = _state.value.clientFirstSurname,
        phone: String = _state.value.clientPhone,
        email: String = _state.value.clientEmail
    ): Boolean {
        return name.length >= 3 && firstSurname.length >= 3 && Patterns.PHONE.matcher(phone).matches() && Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

    fun onOrderChange(order: String) {
        _state.update { currentState ->
            val digits = order.filter(Char::isDigit)
            val isFollowEnabled = orderDigitsRegex.matches(digits)

            currentState.copy(
                orderInput = digits,
                followedOrder = null,
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
                toQuoteInput = toQuote.filter(Char::isDigit), toQuote = String.format(Locale.getDefault(), "%.2f", total)
            )
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
            currentState.copy(clientPhone = phone.filter(Char::isDigit), isSaveEnabled = validateClient(phone = phone))
        }
    }

    fun onClientEmailChange(email: String) {
        _state.update { currentState ->
            currentState.copy(clientEmail = email.replace(" ", ""), isSaveEnabled = validateClient(email = email))
        }
    }

    fun followOrder() {
        _state.value =
            _state.value.copy(isLoading = true, order = null, message = null, error = null)
        viewModelScope.launch {
            val orderDigits = _state.value.orderInput.filter(Char::isDigit)
            if (!orderDigitsRegex.matches(orderDigits)) {
                _state.value = _state.value.copy(
                    error = "Código de pedido invalido. Ingresa solo números.",
                    isLoading = false
                )
                return@launch
            }
            val token = requireAuthToken() ?: return@launch
            try {
                val response = getOrderByIdUseCase(orderDigits, token)
                _state.value = _state.value.copy(followedOrder = response, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun getOrder(orderCode: String) {
        _state.value =
            _state.value.copy(isLoading = true, followedOrder = null, message = null, error = null)
        viewModelScope.launch {
            val token = requireAuthToken() ?: return@launch
            try {
                val response = getOrderByIdUseCase(orderCode, token)
                _state.value = _state.value.copy(order = response, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun getOrders() {
        val clientMongoId = _state.value.clientMongoId
        val clientNumericId = _state.value.clientId
        val clientIdentifier = when {
            !clientMongoId.isNullOrBlank() -> clientMongoId
            clientNumericId != null && clientNumericId > 0 -> clientNumericId.toString()
            else -> return
        }
        val search = _state.value.searchInput.ifBlank { null }

        _state.value = _state.value.copy(isLoading = true, message = null, error = null)
        viewModelScope.launch {
            val token = requireAuthToken() ?: return@launch
            try {
                val response = getOrdersByClientUseCase(clientId = clientIdentifier, token = token, search = search)
                _state.value = _state.value.copy(orders = response, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun getServices() {
        _state.value = _state.value.copy(isLoading = true, message = null, error = null)
        viewModelScope.launch {
            val token = requireAuthToken() ?: return@launch
            try {
                val response = getServicesUseCase(token)
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

    fun setSession(client: Client?, token: String?) {
        _state.update { currentState ->
            currentState.copy(
                clientId = client?.id_cliente,
                clientMongoId = client?.mongo_id,
                clientName = client?.nombre?.trim() ?: "",
                clientFirstSurname = client?.primer_apellido?.trim() ?: "",
                clientSecondSurname = client?.segundo_apellido?.trim() ?: "",
                clientPhone = client?.telefono ?: "",
                clientEmail = client?.correo ?: "",
                clientStatus = client?.estado ?: if (client?.activo == false) "inactivo" else "activo",
                clientProfileImage = client?.imagen_perfil ?: "",
                authToken = token,
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
            val numericClientId = _state.value.clientId
            val updateClientId = when {
                !_state.value.clientMongoId.isNullOrBlank() -> _state.value.clientMongoId!!
                numericClientId != null && numericClientId > 0 -> numericClientId.toString()
                else -> null
            }

            if (updateClientId == null) {
                _state.value = _state.value.copy(
                    error = "No se encontró identificador de cliente para actualizar.",
                    isLoading = false
                )
                return@launch
            }

            val updateClient = UpdateClient(
                nombre = _state.value.clientName,
                primer_apellido = _state.value.clientFirstSurname,
                segundo_apellido = _state.value.clientSecondSurname,
                telefono = _state.value.clientPhone,
                correo = _state.value.clientEmail,
                estado = _state.value.clientStatus,
                imagen_perfil = _state.value.clientProfileImage
            )

            val token = requireAuthToken() ?: return@launch
            try {
                val response = putUpdateClientUseCase(updateClientId, updateClient, token)

                _state.value = _state.value.copy(
                    message = response, client = _state.value.client?.copy(
                        nombre = updateClient.nombre,
                        primer_apellido = updateClient.primer_apellido,
                        segundo_apellido = updateClient.segundo_apellido,
                        telefono = updateClient.telefono,
                        correo = updateClient.correo,
                        estado = updateClient.estado,
                        imagen_perfil = updateClient.imagen_perfil,
                        activo = updateClient.estado.equals("activo", ignoreCase = true)
                    ), isEditEnabled = false, isChangePasswordEnabled = false, isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun setEditModes(enabled: Boolean) {
        _state.update { it.copy(isEditEnabled = enabled, isChangePasswordEnabled = enabled) }
        if (!enabled) {
            resetClient()
            resetPasswordFields()
        }
    }

    fun editEnabled(enabled: Boolean) {
        _state.value = _state.value.copy(isEditEnabled = enabled)
    }

    fun changePasswordEnabled(enabled: Boolean) {
        _state.value = _state.value.copy(isChangePasswordEnabled = enabled)
    }

    fun onCurrentPasswordChange(password: String) {
        _state.value = _state.value.copy(currentPassword = password)
    }

    fun onNewPasswordChange(password: String) {
        _state.value = _state.value.copy(newPassword = password)
    }

    fun onConfirmPasswordChange(password: String) {
        _state.value = _state.value.copy(confirmPassword = password)
    }

    fun changePassword() {
        val currentPassword = _state.value.currentPassword
        val newPassword = _state.value.newPassword
        val confirmPassword = _state.value.confirmPassword

        if (currentPassword.isBlank()) {
            _state.value = _state.value.copy(error = "Ingresa tu contraseña actual")
            return
        }
        if (newPassword.isBlank()) {
            _state.value = _state.value.copy(error = "Ingresa tu nueva contraseña")
            return
        }
        if (newPassword.length < 6) {
            _state.value = _state.value.copy(error = "La contraseña debe tener al menos 6 caracteres")
            return
        }
        if (newPassword != confirmPassword) {
            _state.value = _state.value.copy(error = "Las contraseñas no coinciden")
            return
        }
        if (currentPassword == newPassword) {
            _state.value = _state.value.copy(error = "La nueva contraseña debe ser diferente a la actual")
            return
        }

        _state.value = _state.value.copy(isLoading = true, error = null, message = null)
        viewModelScope.launch {
            val clientMongoId = _state.value.clientMongoId ?: return@launch
            val token = requireAuthToken() ?: return@launch
            try {
                val changePassword = ChangePassword(
                    contrasena_actual = currentPassword,
                    contrasena_nueva = newPassword
                )
                val response = patchChangePasswordClientUseCase(clientMongoId, changePassword, token)
                _state.value = _state.value.copy(
                    message = response,
                    currentPassword = "",
                    newPassword = "",
                    confirmPassword = "",
                    isChangePasswordEnabled = false,
                    isEditEnabled = false,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun resetPasswordFields() {
        _state.value = _state.value.copy(
            currentPassword = "",
            newPassword = "",
            confirmPassword = "",
            isChangePasswordEnabled = false,
            isEditEnabled = false,
            message = null,
            error = null
        )
    }

    fun resetOrder() {
        _state.value = _state.value.copy(
            order = null, message = null, error = null
        )
    }

    fun resetClient() {
        _state.value = _state.value.copy(
            clientId = _state.value.client?.id_cliente,
            clientMongoId = _state.value.client?.mongo_id,
            clientName = _state.value.client?.nombre ?: "",
            clientFirstSurname = _state.value.client?.primer_apellido ?: "",
            clientSecondSurname = _state.value.client?.segundo_apellido ?: "",
            clientPhone = _state.value.client?.telefono ?: "",
            clientEmail = _state.value.client?.correo ?: "",
            clientStatus = _state.value.client?.estado ?: if (_state.value.client?.activo == false) "inactivo" else "activo",
            clientProfileImage = _state.value.client?.imagen_perfil ?: "",
            isEditEnabled = false,
            isChangePasswordEnabled = false,
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
    val clientMongoId: String? = null,
    val clientName: String = "",
    val clientFirstSurname: String = "",
    val clientSecondSurname: String = "",
    val clientPhone: String = "",
    val clientEmail: String = "",
    val clientStatus: String = "activo",
    val clientProfileImage: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val authToken: String? = null,
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
    val isChangePasswordEnabled: Boolean = false,
    val isLoading: Boolean = false
)