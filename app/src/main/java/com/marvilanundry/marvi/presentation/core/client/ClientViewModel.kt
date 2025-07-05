import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvilanundry.marvi.domain.model.Client
import com.marvilanundry.marvi.domain.usecase.GetClientByCodeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClientViewModel(
    private val getClientByCodeUseCase: GetClientByCodeUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ClientUiState())
    val state: StateFlow<ClientUiState> = _state

    fun loadClient(code: String) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val client = getClientByCodeUseCase(code)
                _state.value = ClientUiState(client = client, isLoading = false)
            } catch (e: Exception) {
                _state.value =
                    ClientUiState(error = e.message ?: "Error desconocido", isLoading = false)
            }
        }
    }
}

data class ClientUiState(
    val client: Client? = null, val isLoading: Boolean = false, val error: String? = null
)