package com.marvilanundry.marvi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.marvilanundry.marvi.presentation.core.navigation.NavigationWrapper
import com.marvilanundry.marvi.ui.theme.MARVITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MARVITheme {
                NavigationWrapper()
            }
        }
    }
}