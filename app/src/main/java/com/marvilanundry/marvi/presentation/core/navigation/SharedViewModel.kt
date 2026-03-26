package com.marvilanundry.marvi.presentation.core.navigation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.marvilanundry.marvi.domain.model.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _client = mutableStateOf<Client?>(null)
    val client: State<Client?> = _client
    private val _authToken = mutableStateOf<String?>(null)
    val authToken: State<String?> = _authToken

    fun setSession(client: Client, token: String?) {
        _client.value = client
        _authToken.value = token
    }

    fun clearSession() {
        _client.value = null
        _authToken.value = null
    }

    fun setClient(client: Client) {
        setSession(client, null)
    }
}