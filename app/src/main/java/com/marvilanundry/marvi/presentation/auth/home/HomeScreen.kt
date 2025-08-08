package com.marvilanundry.marvi.presentation.auth.home

import MARVIFloatingActionButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.domain.model.Client
import com.marvilanundry.marvi.presentation.auth.home.screens.AccountScreen
import com.marvilanundry.marvi.presentation.auth.home.screens.FollowScreen
import com.marvilanundry.marvi.presentation.auth.home.screens.OrdersScreen
import com.marvilanundry.marvi.presentation.auth.home.screens.ToQuoteScreen
import com.marvilanundry.marvi.presentation.core.components.MARVIBottomNavigationBar
import com.marvilanundry.marvi.presentation.core.components.MARVIDatePicker
import com.marvilanundry.marvi.presentation.core.components.MARVIDialog
import com.marvilanundry.marvi.presentation.core.components.MARVIDialogType
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun HomeScreen(client: Client?, onNavigateToLogin: () -> Unit = {}) {
    // Constantes
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
    val cancelTitle = stringResource(R.string.marvi_home_dialog_cancel_title)
    val cancelMessage = stringResource(R.string.marvi_home_dialog_cancel_message)
    val infoMessage = stringResource(R.string.marvi_home_dialog_info_message)

    // Variables
    var showDialogQuestion by remember { mutableStateOf(false) }
    var showDialogSuccess by remember { mutableStateOf(false) }
    var showDialogError by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var disableScreen by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }

    // Botón de retroceso
    BackHandler(true) {
        showDialogQuestion = true
    }

    // Guarda el cliente en el ViewModel
    LaunchedEffect(Unit) {
        homeViewModel.setClient(client)
    }

    // Si el cliente es nulo, redirige al login
    LaunchedEffect(client) {
        if (client == null) onNavigateToLogin()

    }

    // Limpia el foco y resetea el cliente al cambiar de página
    LaunchedEffect(pagerState.currentPage) {
        focusManager.clearFocus()
        if (pagerState.currentPage != 3) homeViewModel.resetClient()
    }

    // Observa los cambios en el estado del ViewModel
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
                    dialogTitle = cancelTitle
                    dialogMessage = String.format(cancelMessage, followedOrder.id_pedido)
                    showDialogError = true
                }
            }

            order != null -> {
                val orderDetails =
                    homeViewModelState.orders?.find { it.id_pedido == order.id_pedido }?.detalles

                dialogMessage = String.format(
                    infoMessage,
                    order.id_pedido.toString(),
                    order.fecha_pedido,
                    order.fecha_entrega,
                    orderDetails,
                    String.format(Locale.getDefault(), "%.2f", order.total)
                )
            }
        }
    }

    // Muestra el diálogo correspondiente
    when {
        showDialogLoading -> {
            MARVIDialog(
                type = MARVIDialogType.LOADING,
                title = stringResource(R.string.marvi_core_dialog_loading_title),
                message = stringResource(R.string.marvi_core_dialog_loading_message)
            )
        }

        showDialogQuestion -> {
            MARVIDialog(
                type = MARVIDialogType.QUESTION,
                title = stringResource(R.string.marvi_home_dialog_question_title),
                message = stringResource(R.string.marvi_home_dialog_question_message),
                confirmButtonText = stringResource(R.string.marvi_home_dialog_question_confirm_button),
                dismissButtonText = stringResource(R.string.marvi_home_dialog_question_dismiss_button),
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
                title = stringResource(R.string.marvi_home_dialog_success_title),
                message = dialogMessage,
                confirmButtonText = stringResource(R.string.marvi_home_dialog_success_confirm_button),
                onConfirm = {
                    showDialogSuccess = false
                })
        }

        showDialogError -> {
            MARVIDialog(
                type = MARVIDialogType.ERROR,
                title = dialogTitle,
                message = dialogMessage,
                confirmButtonText = stringResource(R.string.marvi_home_dialog_error_confirm_button),
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
                title = stringResource(R.string.marvi_home_dialog_info_title),
                message = dialogMessage,
                textAlign = TextAlign.Start,
                confirmButtonText = stringResource(R.string.marvi_home_dialog_info_confirm_button),
                onConfirm = {
                    homeViewModel.resetOrder()
                },
                onDismiss = {
                    homeViewModel.resetOrder()
                })
        }

        showDatePicker -> {
            MARVIDatePicker(onDateSelected = { date ->
                if (date != null) {
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.timeInMillis = date

                    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                        timeZone = TimeZone.getTimeZone("UTC")
                    }.format(calendar.time)

                    homeViewModel.onSearchChange(formattedDate)
                    homeViewModel.getOrders()
                }
                showDatePicker = false
            }, onDismiss = {
                showDatePicker = false
            })
        }
    }

    // Composable principal
    Scaffold(bottomBar = {
        MARVIBottomNavigationBar(pagerState = pagerState) { page ->
            coroutineScope.launch {
                pagerState.scrollToPage(page)
            }
        }
    }, floatingActionButton = {
        when (pagerState.currentPage) {
            1 -> {
                MARVIFloatingActionButton(
                    icon = R.drawable.ic_calendar_date,
                ) { showDatePicker = true }
            }

            3 -> {
                if (!homeViewModelState.isEditEnabled) {
                    MARVIFloatingActionButton(
                        icon = R.drawable.ic_pencil_square,
                    ) {
                        homeViewModel.editEnabled(true)
                    }
                }
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