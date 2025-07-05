package com.marvilanundry.marvi

import ClientViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.marvilanundry.marvi.data.remote.ApiService
import com.marvilanundry.marvi.data.repository.ClientRepositoryImpl
import com.marvilanundry.marvi.domain.usecase.GetClientByCodeUseCase
import com.marvilanundry.marvi.presentation.core.navigation.NavigationWrapper
import com.marvilanundry.marvi.ui.theme.MARVITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val api = ApiService()
        val repo = ClientRepositoryImpl(api)
        val useCase = GetClientByCodeUseCase(repo)
        val clientViewModel = ClientViewModel(useCase)

        setContent {
            MARVITheme {
                NavigationWrapper(clientViewModel)
            }
        }
    }
}