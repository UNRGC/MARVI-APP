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

    private fun isValidUser(code: String, name: String, firstSurname: String): Boolean {
        val isCodeValid = code.isNotBlank() && code.length >= 3
        val isNameValid = name.isNotBlank() && name.length >= 3
        val isFirstSurnameValid = firstSurname.isNotBlank() && firstSurname.length >= 3
        return isCodeValid && isNameValid && isFirstSurnameValid
    }

    private fun isValidRegister(phone: String, email: String, password: String): Boolean {
        val isPhoneValid = phone.length == 10
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 8
        return isPhoneValid && isEmailValid && isPasswordValid
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
            val stateWithCodeChanged = currentState.copy(code = code)

            val nextEnabled = isValidUser(
                stateWithCodeChanged.code,
                stateWithCodeChanged.name,
                stateWithCodeChanged.firstSurname
            )

            val progress = calculateProgress(stateWithCodeChanged)

            stateWithCodeChanged.copy(
                isNextEnabled = nextEnabled, progressBar = progress
            )
        }
    }

    fun onNameChange(name: String) {
        _state.update { currentState ->
            val stateWithNameChanged = currentState.copy(name = name)

            val nextEnabled = isValidUser(
                stateWithNameChanged.code,
                stateWithNameChanged.name,
                stateWithNameChanged.firstSurname
            )

            val progress = calculateProgress(stateWithNameChanged)

            stateWithNameChanged.copy(
                isNextEnabled = nextEnabled, progressBar = progress
            )
        }
    }

    fun onFirstSurnameChange(firstSurname: String) {
        _state.update { currentState ->
            val stateWithFirstSurnameChanged = currentState.copy(firstSurname = firstSurname)

            val nextEnabled = isValidUser(
                stateWithFirstSurnameChanged.code,
                stateWithFirstSurnameChanged.name,
                stateWithFirstSurnameChanged.firstSurname
            )

            val progress = calculateProgress(stateWithFirstSurnameChanged)

            stateWithFirstSurnameChanged.copy(
                isNextEnabled = nextEnabled, progressBar = progress
            )
        }
    }

    fun onSecondSurnameChange(secondSurname: String) {
        _state.update { currentState ->
            val stateWithSecondSurnameChanged = currentState.copy(secondSurname = secondSurname)

            val nextEnabled = isValidUser(
                stateWithSecondSurnameChanged.code,
                stateWithSecondSurnameChanged.name,
                stateWithSecondSurnameChanged.firstSurname
            )

            val progress = calculateProgress(stateWithSecondSurnameChanged)

            stateWithSecondSurnameChanged.copy(
                isNextEnabled = nextEnabled, progressBar = progress
            )
        }
    }

    fun onPhoneChange(phone: String) {
        _state.update { currentState ->
            val stateWithPhoneChanged = currentState.copy(phone = phone)

            val registerEnabled = isValidRegister(
                stateWithPhoneChanged.phone,
                stateWithPhoneChanged.email,
                stateWithPhoneChanged.password
            )

            val progress = calculateProgress(stateWithPhoneChanged)

            stateWithPhoneChanged.copy(
                isRegisterEnabled = registerEnabled, progressBar = progress
            )
        }
    }

    fun onEmailChange(email: String) {
        _state.update { currentState ->
            val stateWithEmailChanged = currentState.copy(email = email)

            val registerEnabled = isValidRegister(
                stateWithEmailChanged.phone,
                stateWithEmailChanged.email,
                stateWithEmailChanged.password
            )

            val progress = calculateProgress(stateWithEmailChanged)

            stateWithEmailChanged.copy(
                isRegisterEnabled = registerEnabled, progressBar = progress
            )
        }
    }

    fun onPasswordChange(password: String) {
        _state.update { currentState ->
            val stateWithPasswordChanged = currentState.copy(password = password)

            val registerEnabled = isValidRegister(
                stateWithPasswordChanged.phone,
                stateWithPasswordChanged.email,
                stateWithPasswordChanged.password
            )

            val progress = calculateProgress(stateWithPasswordChanged)

            stateWithPasswordChanged.copy(
                isRegisterEnabled = registerEnabled, progressBar = progress
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
                nombre = _state.value.name,
                primer_apellido = _state.value.firstSurname,
                segundo_apellido = _state.value.secondSurname.ifBlank { null },
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