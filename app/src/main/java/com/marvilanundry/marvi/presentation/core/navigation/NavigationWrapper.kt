package com.marvilanundry.marvi.presentation.core.navigation

import ClientViewModel
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marvilanundry.marvi.presentation.auth.recovery.RecoveryScreen
import com.marvilanundry.marvi.presentation.auth.login.LoginScreen
import com.marvilanundry.marvi.presentation.auth.register.RegisterScreen

@Composable
fun NavigationWrapper(clientViewModel: ClientViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Login,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable<Login> {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Register)
                },
                onNavigateToForgot = {
                    navController.navigate(Recovery)
                },
            )
        }
        composable<Register>(enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
            ) + fadeIn()
        }, exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
            ) + fadeOut()
        }) {
            RegisterScreen(
                clientViewModel = clientViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                })
        }
        composable<Recovery>(enterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
            ) + fadeIn()
        }, exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
            ) + fadeOut()
        }) {
            RecoveryScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                })
        }
    }
}