package com.marvilanundry.marvi.presentation.core.navigation

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
import com.marvilanundry.marvi.presentation.auth.home.HomeScreen
import com.marvilanundry.marvi.presentation.auth.login.LoginScreen
import com.marvilanundry.marvi.presentation.auth.recovery.RecoveryScreen
import com.marvilanundry.marvi.presentation.auth.register.RegisterScreen
import com.marvilanundry.marvi.presentation.auth.welcome.WelcomeScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Welcome,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable<Welcome> {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(Login) {
                        popUpTo(Welcome) { inclusive = true }
                    }
                })
        }
        composable<Login> {
            LoginScreen(onNavigateToRegister = {
                navController.navigate(Register)
            }, onNavigateToForgot = {
                navController.navigate(Recovery)
            }, onNavigateToHome = {
                navController.navigate(Home) {
                    popUpTo(Login) { inclusive = true }
                }
            })
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
        composable<Home>(enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
            ) + fadeIn()
        }, exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
            ) + fadeOut()
        }) {
            HomeScreen(
                onNavigateToLogin = {}
            )
        }
    }
}