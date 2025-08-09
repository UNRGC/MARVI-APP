package com.marvilanundry.marvi.presentation.auth.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvilanundry.marvi.domain.model.NewClient
import com.marvilanundry.marvi.domain.usecase.GetClientCodeUseCase
import com.marvilanundry.marvi.domain.usecase.PostNewClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val getClientCodeUseCase: GetClientCodeUseCase,
    private val postNewClientUseCase: PostNewClientUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state

    private fun isValidUser(
        code: String = _state.value.code,
        name: String = _state.value.name,
        firstSurname: String = _state.value.firstSurname
    ): Boolean {
        return code.length >= 3 && name.length >= 3 && firstSurname.length >= 3
    }

    private fun isValidRegister(
        phone: String = _state.value.phone,
        email: String = _state.value.email,
        password: String = _state.value.password
    ): Boolean {
        return Patterns.PHONE.matcher(phone).matches() && Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() && password.length >= 8
    }

    private fun calculateProgress(updatedState: RegisterUiState): Float {
        return listOf(
            updatedState.code,
            updatedState.name,
            updatedState.firstSurname,
            updatedState.phone,
            updatedState.email,
            updatedState.password,
        ).count { it.isNotBlank() && it.length >= 3 } / 6f
    }

    fun onCodeChange(code: String) {
        _state.update { currentState ->
            val stateWithCodeChanged = currentState.copy(code = code.replace(" ", ""))
            val progress = calculateProgress(stateWithCodeChanged)

            stateWithCodeChanged.copy(
                isNextEnabled = isValidUser(code = code), progressBar = progress
            )
        }
    }

    fun onNameChange(name: String) {
        _state.update { currentState ->
            val stateWithNameChanged = currentState.copy(name = name)
            val progress = calculateProgress(stateWithNameChanged)

            stateWithNameChanged.copy(
                isNextEnabled = isValidUser(name = name), progressBar = progress
            )
        }
    }

    fun onFirstSurnameChange(firstSurname: String) {
        _state.update { currentState ->
            val stateWithFirstSurnameChanged = currentState.copy(firstSurname = firstSurname)
            val progress = calculateProgress(stateWithFirstSurnameChanged)

            stateWithFirstSurnameChanged.copy(
                isNextEnabled = isValidUser(firstSurname = firstSurname), progressBar = progress
            )
        }
    }

    fun onSecondSurnameChange(secondSurname: String) {
        _state.update { currentState ->
            val stateWithSecondSurnameChanged = currentState.copy(secondSurname = secondSurname)
            val progress = calculateProgress(stateWithSecondSurnameChanged)

            stateWithSecondSurnameChanged.copy(
                isNextEnabled = isValidUser(), progressBar = progress
            )
        }
    }

    fun onPhoneChange(phone: String) {
        _state.update { currentState ->
            val stateWithPhoneChanged = currentState.copy(phone = phone.filter(Char::isDigit))
            val progress = calculateProgress(stateWithPhoneChanged)

            stateWithPhoneChanged.copy(
                isRegisterEnabled = isValidRegister(phone = phone), progressBar = progress
            )
        }
    }

    fun onEmailChange(email: String) {
        _state.update { currentState ->
            val stateWithEmailChanged = currentState.copy(email = email.replace(" ", ""))
            val progress = calculateProgress(stateWithEmailChanged)

            stateWithEmailChanged.copy(
                isRegisterEnabled = isValidRegister(email = email), progressBar = progress
            )
        }
    }

    fun onPasswordChange(password: String) {
        _state.update { currentState ->
            val stateWithPasswordChanged = currentState.copy(password = password.replace(" ", ""))
            val progress = calculateProgress(stateWithPasswordChanged)

            stateWithPasswordChanged.copy(
                isRegisterEnabled = isValidRegister(password = password), progressBar = progress
            )
        }
    }

    fun onSectionChange(section: Int) {
        _state.update { currentState ->
            currentState.copy(section = section)
        }
    }

    fun checkCode() {
        _state.value =
            _state.value.copy(isLoading = true, error = null, warning = null, message = null)
        viewModelScope.launch {
            try {
                val response = getClientCodeUseCase(_state.value.code)

                _state.value = _state.value.copy(warning = response, isLoading = false)
            } catch (e: Exception) {
                if (e.message?.contains("El código no existe") == true) _state.value =
                    _state.value.copy(section = 1, isLoading = false)
                else _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun register() {
        _state.value =
            _state.value.copy(isLoading = true, error = null, warning = null, message = null)
        viewModelScope.launch {
            val newClient = NewClient(
                codigo = _state.value.code,
                nombre = _state.value.name.trim(),
                primer_apellido = _state.value.firstSurname.trim(),
                segundo_apellido = _state.value.secondSurname.trim().ifBlank { null },
                telefono = _state.value.phone,
                correo = _state.value.email,
                contrasena = _state.value.password
            )

            try {
                val response = postNewClientUseCase(newClient)

                _state.value = _state.value.copy(message = response, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}

data class RegisterUiState(
    val progressBar: Float = 0f,
    val section: Int = 0,
    val code: String = "",
    val name: String = "",
    val firstSurname: String = "",
    val secondSurname: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val message: String? = null,
    val warning: String? = null,
    val error: String? = null,
    val isNextEnabled: Boolean = false,
    val isRegisterEnabled: Boolean = false,
    val isLoading: Boolean = false,
)