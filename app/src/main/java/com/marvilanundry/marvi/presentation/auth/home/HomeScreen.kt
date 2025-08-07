package com.marvilanundry.marvi.presentation.auth.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marvilanundry.marvi.presentation.auth.home.screens.AccountScreen
import com.marvilanundry.marvi.presentation.auth.home.screens.FollowScreen
import com.marvilanundry.marvi.presentation.auth.home.screens.OrdersScreen
import com.marvilanundry.marvi.presentation.auth.home.screens.ToQuoteScreen
import com.marvilanundry.marvi.presentation.core.navigation.SharedViewModel
import com.marvilanundry.marvi.presentation.core.components.MARVIBottomNavigationBar
import com.marvilanundry.marvi.presentation.core.components.MARVIDialog
import com.marvilanundry.marvi.presentation.core.components.MARVIDialogType
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(sharedViewModel: SharedViewModel, onNavigateToLogin: () -> Unit = {}) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeViewModelState by homeViewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()
    val showDialogLoading = homeViewModelState.isLoading
    val showDialogInfo = homeViewModelState.order != null
    val message = homeViewModelState.message?.split(",", limit = 2)
    val error = homeViewModelState.error?.split(",", limit = 2)
    val followedOrder = homeViewModelState.followedOrder
    val order = homeViewModelState.order

    var showDialogQuestion by remember { mutableStateOf(false) }
    var showDialogSuccess by remember { mutableStateOf(false) }
    var showDialogError by remember { mutableStateOf(false) }
    var disableScreen by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }

    BackHandler(true) {
        showDialogQuestion = true
    }

    LaunchedEffect(Unit) {
        homeViewModel.setClient(sharedViewModel.client.value)
    }

    LaunchedEffect(homeViewModelState.isLoading) {
        when {
            message != null -> {
                dialogMessage = if (message.size > 1) message[1].trim() else message[0]
                showDialogSuccess = true
            }

            error != null -> {
                dialogTitle = error[0]
                dialogMessage = if (error.size > 1) error[1].trim() else error[0]
                showDialogError = true
            }

            followedOrder != null -> {
                if (followedOrder.estado == "Cancelado") {
                    dialogTitle = "Pedido cancelado"
                    dialogMessage = "el pedido #${followedOrder.id_pedido} fue cancelado."
                    showDialogError = true
                }
            }

            order != null -> {
                val orderDetails = homeViewModelState.orders?.find { it.id_pedido == order.id_pedido }?.detalles

                dialogMessage = "Número de pedido: ${order.id_pedido}\nFecha de creación: ${order.fecha_pedido}\nFecha de entrega: ${order.fecha_entrega}\nDetalles: ${orderDetails ?: "N/A"}\nTotal: $${String.format("%.2f", order.total)}"
            }
        }
    }

    when {
        showDialogLoading -> {
            MARVIDialog(
                type = MARVIDialogType.LOADING,
                title = "Cargando...",
                message = "por favor, espera mientras se procesa tu información"
            )
        }

        showDialogQuestion -> {
            MARVIDialog(
                type = MARVIDialogType.QUESTION,
                title = "Cerrar sesión",
                message = "¿Deseas cerrar sesión?",
                confirmButtonText = "Si",
                dismissButtonText = "No",
                onConfirm = {
                    disableScreen = true
                    showDialogQuestion = false
                    onNavigateToLogin()
                },
                onDismiss = {
                    showDialogQuestion = false
                })
        }

        showDialogSuccess -> {
            MARVIDialog(
                type = MARVIDialogType.SUCCESS,
                title = "Éxito",
                message = dialogMessage,
                confirmButtonText = "Aceptar",
                onConfirm = {
                    disableScreen = true
                    showDialogSuccess = false
                })
        }

        showDialogError -> {
            MARVIDialog(
                type = MARVIDialogType.ERROR,
                title = dialogTitle,
                message = dialogMessage,
                confirmButtonText = "Aceptar",
                onConfirm = {
                    showDialogError = false
                },
                onDismiss = {
                    showDialogError = false
                })
        }

        showDialogInfo -> {
            MARVIDialog(
                type = MARVIDialogType.SUCCESS,
                title = "Información del pedido",
                message = dialogMessage,
                textAlign = TextAlign.Start,
                confirmButtonText = "Cerrar",
                onConfirm = {
                    homeViewModel.resetOrder()
                },
                onDismiss = {
                    homeViewModel.resetOrder()
                })
        }
    }

    Scaffold(
        bottomBar = {
            MARVIBottomNavigationBar(pagerState = pagerState) { page ->
                coroutineScope.launch {
                    pagerState.scrollToPage(page)
                }
            }
        }) { padding ->
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        focusManager.clearFocus()
                    }, contentAlignment = Alignment.TopCenter
            ) {
                when (page) {
                    0 -> FollowScreen(homeViewModel, homeViewModelState)
                    1 -> OrdersScreen(homeViewModel, homeViewModelState)
                    2 -> ToQuoteScreen(homeViewModel, homeViewModelState)
                    3 -> AccountScreen(homeViewModel, homeViewModelState)
                }

                if (disableScreen) Box(
                    modifier = Modifier
                        .matchParentSize()
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    awaitPointerEvent()
                                }
                            }
                        })
            }
        }
    }
}