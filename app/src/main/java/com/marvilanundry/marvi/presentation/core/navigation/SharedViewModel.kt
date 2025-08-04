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

    fun setClient(client: Client) {
        _client.value = client
    }
}