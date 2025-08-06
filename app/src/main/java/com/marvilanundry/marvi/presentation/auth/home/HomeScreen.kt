package com.marvilanundry.marvi.presentation.auth.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.presentation.core.navigation.SharedViewModel
import com.marvilanundry.marvi.presentation.core.components.MARVIBottomNavigationBar
import com.marvilanundry.marvi.presentation.core.components.MARVIButton
import com.marvilanundry.marvi.presentation.core.components.MARVIButtonType
import com.marvilanundry.marvi.presentation.core.components.MARVIDialog
import com.marvilanundry.marvi.presentation.core.components.MARVIDialogType
import com.marvilanundry.marvi.presentation.core.components.MARVITextField
import com.marvilanundry.marvi.ui.theme.CustomColors
import kotlinx.coroutines.launch

@Composable
fun Container(content: @Composable ColumnScope.() -> Unit) {
    val shape = MaterialTheme.shapes.small
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = shape)
            .background(CustomColors.backgroundVariant, shape = shape)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content
        )
    }
}

@Composable
fun ColumnScrollable(
    scrollState: ScrollState = rememberScrollState(),
    paddingValues: PaddingValues,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}

@Composable
fun SectionHeader(iconRes: Int, title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes), contentDescription = title
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ClearableTextField(
    label: String? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLength: Int = 100
) {
    MARVITextField(
        label = label,
        modifier = modifier,
        value = value,
        placeholder = placeholder,
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_x),
                contentDescription = label,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        onValueChange("")
                    },
                tint = MaterialTheme.colorScheme.outlineVariant
            )
        },
        keyboardType = keyboardType,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLength = maxLength,
        onValueChange = onValueChange
    )
}

data class Status(val icon: Int, val title: String, val message: String)

@Composable
fun FollowScreen(homeViewModel: HomeViewModel, homeViewModelState: HomeUiState) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val statusItems = listOf(
        Status(
            R.drawable.ic_shop_window,
            stringResource(id = R.string.marvi_follow_status_one),
            stringResource(id = R.string.marvi_follow_status_one_message)
        ), Status(
            R.drawable.ic_box2_heart,
            stringResource(id = R.string.marvi_follow_status_two),
            stringResource(id = R.string.marvi_follow_status_two_message)
        ), Status(
            R.drawable.ic_bag_check,
            stringResource(id = R.string.marvi_follow_status_three),
            stringResource(id = R.string.marvi_follow_status_three_message)
        )
    )

    ColumnScrollable(
        scrollState = scrollState, paddingValues = PaddingValues(24.dp, 24.dp, 24.dp, 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.height(32.dp)
        )
        Container {
            SectionHeader(
                iconRes = R.drawable.ic_card_checklist,
                title = stringResource(id = R.string.marvi_follow_title)
            )
            ClearableTextField(
                label = stringResource(id = R.string.marvi_follow_order),
                value = homeViewModelState.orderInput,
                onValueChange = { homeViewModel.onOrderChange(it) },
                placeholder = stringResource(id = R.string.marvi_follow_order_placeholder),
                keyboardType = KeyboardType.Number,
                keyboardActions = KeyboardActions(
                    onDone = {
                        homeViewModel.followOrder()
                    })
            )
            MARVIButton(
                text = stringResource(id = R.string.marvi_follow_button),
                modifier = Modifier.fillMaxWidth(),
                enabled = homeViewModelState.isFollowEnabled,
                message = stringResource(id = R.string.marvi_follow_button_message),
                onClick = {
                    homeViewModel.followOrder()
                })
        }
        Text(
            text = stringResource(id = R.string.marvi_follow_status_title),
            textAlign = TextAlign.Center
        )
        val currentStatusIndex =
            statusItems.indexOfFirst { it.title == homeViewModelState.followedOrder?.estado }
        statusItems.forEachIndexed { index, item ->
            val ready = index <= currentStatusIndex && currentStatusIndex != -1

            if (ready) {
                LocalFocusManager.current.clearFocus()
                coroutineScope.launch {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start)
            ) {
                Icon(
                    painter = if (ready) painterResource(id = R.drawable.ic_check_circle_fill) else painterResource(
                        id = R.drawable.ic_circle
                    ),
                    contentDescription = null,
                    tint = if (ready) MaterialTheme.colorScheme.primary else CustomColors.placeholderColor
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.small,
                        )
                        .background(
                            color = CustomColors.backgroundVariant,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start)
                ) {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp),
                        tint = if (ready) MaterialTheme.colorScheme.primary else CustomColors.placeholderColor
                    )
                    Column {
                        Text(
                            text = item.title,
                            color = if (ready) MaterialTheme.colorScheme.primary else CustomColors.placeholderColor
                        )
                        Text(
                            text = item.message,
                            color = if (ready) CustomColors.textColor else CustomColors.placeholderColor,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrdersScreen(
    homeViewModel: HomeViewModel, homeViewModelState: HomeUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp, 24.dp, 24.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Container {
            SectionHeader(
                iconRes = R.drawable.ic_clock_history, title = "Historial de pedidos"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ClearableTextField(
                    modifier = Modifier.weight(1f),
                    value = homeViewModelState.searchInput,
                    placeholder = "Buscar en pedidos",
                    keyboardType = KeyboardType.Uri,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            homeViewModel.getOrders()
                        }),
                    onValueChange = {
                        homeViewModel.onSearchChange(it)
                    })
                MARVIButton(
                    modifier = Modifier.size(48.dp),
                    type = MARVIButtonType.ICON,
                    iconRes = R.drawable.ic_search,
                    onClick = {
                        homeViewModel.getOrders()
                    })
            }
        }
        if (homeViewModelState.orders != null && homeViewModelState.orders.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(0.dp, 0.dp, 0.dp, 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(homeViewModelState.orders.size) { index ->
                    val order = homeViewModelState.orders[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.small
                            )
                            .background(
                                color = CustomColors.backgroundVariant,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(16.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                homeViewModel.getOrder(order.id_pedido)
                            },
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(MaterialTheme.colorScheme.secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_box2),
                                contentDescription = "Orders",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "#${order.id_pedido}", fontWeight = FontWeight.Bold
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.small)
                                        .background(MaterialTheme.colorScheme.outline)
                                        .padding(8.dp, 4.dp), contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$${order.total}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            Text(
                                text = order.fecha_pedido,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = order.detalles,
                                color = CustomColors.placeholderColor,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        } else {
            Text(
                text = "No se encontraron pedidos", textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ToQuoteScreen(homeViewModel: HomeViewModel, homeViewModelState: HomeUiState) {
    ColumnScrollable(
        paddingValues = PaddingValues(24.dp, 24.dp, 24.dp, 16.dp)
    ) {
        Container {
            SectionHeader(
                iconRes = R.drawable.ic_cash_coin, title = "Cotizador de servicios"
            )
            Text(
                text = "Tipo de servicio"
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(4) { index ->
                    FilterChip(
                        selected = false,
                        onClick = { },
                        label = {
                            Text(
                                text = when (index) {
                                    0 -> "Lavado normal (Kilo)"
                                    1 -> "Planchado (Pieza)"
                                    2 -> "Tintorería (Pieza)"
                                    else -> "Secado (Kilo)"
                                }
                            )
                        },
                        shape = MaterialTheme.shapes.small,
                        border = null,
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            labelColor = MaterialTheme.colorScheme.onSecondary,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
            ClearableTextField(
                label = "Ingresa la cantidad",
                value = homeViewModelState.toQuoteInput,
                placeholder = "Ejemplo: 1234",
                keyboardType = KeyboardType.Number,
                onValueChange = { homeViewModel.onToQuoteChange(it) },
            )
            MARVIButton(
                text = "Calcular",
                modifier = Modifier.fillMaxWidth(),
                enabled = homeViewModelState.isCalculationEnabled,
                message = "La cantidad debe ser mayor a 0",
            ) { }
        }
        Container {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Total estimado:", fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp, 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$0.00", color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        Text(
            text = "Este es un calculo aproximado, para obtener un precio exacto, por favor visita una de nuestras sucursales.",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AccountScreen(homeViewModel: HomeViewModel, homeViewModelState: HomeUiState) {

    ColumnScrollable(
        paddingValues = PaddingValues(24.dp, 24.dp, 24.dp, 16.dp)
    ) {
        Container {
            SectionHeader(
                iconRes = R.drawable.ic_person_vcard, title = "Información de la cuenta"
            )
            MARVIButton(
                text = "Editar información",
                modifier = Modifier.fillMaxWidth(),
                type = MARVIButtonType.SECONDARY,
                enabled = true,
                message = "Ya hay una edición en curso"
            ) { }
        }
        Container {
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_code),
                value = homeViewModelState.client?.codigo ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_code_placeholder),
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.None,
                singleLine = true,
                onValueChange = {})
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_name),
                value = homeViewModelState.client?.nombre ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_name_placeholder),
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                singleLine = true,
                onValueChange = { })
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_first_surname),
                value = homeViewModelState.client?.primer_apellido ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_first_surname_placeholder),
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                singleLine = true,
                onValueChange = {})
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_second_surname),
                value = homeViewModelState.client?.segundo_apellido ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_second_surname_placeholder),
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                singleLine = true,
                onValueChange = {})
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_phone),
                value = homeViewModelState.client?.telefono ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_phone_placeholder),
                keyboardType = KeyboardType.Phone,
                capitalization = KeyboardCapitalization.None,
                singleLine = true,
                maxLength = 10,
                onValueChange = {})
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_email),
                value = homeViewModelState.client?.correo ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_email_placeholder),
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None,
                singleLine = true,
                onValueChange = {})
            MARVITextField(
                label = stringResource(id = R.string.marvi_register_password),
                value = homeViewModelState.client?.contrasena ?: "",
                readOnly = true,
                placeholder = stringResource(id = R.string.marvi_register_password_placeholder),
                trailingIcon = {
                    Icon(
                        painter = painterResource(
                        id = R.drawable.ic_eye
                    ),
                        contentDescription = stringResource(id = R.string.marvi_register_password),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {},
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password,
                capitalization = KeyboardCapitalization.None,
                singleLine = true,
                maxLength = 20,
                onValueChange = {})
            MARVIButton(
                text = "Guardar cambios",
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                message = "No se han realizado cambios"
            ) { }
        }
    }
}


@Composable
fun HomeScreen(sharedViewModel: SharedViewModel, onNavigateToLogin: () -> Unit = {}) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeViewModelState by homeViewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()
    val showDialogLoading = homeViewModelState.isLoading
    val message = homeViewModelState.message?.split(",", limit = 2)
    val error = homeViewModelState.error?.split(",", limit = 2)
    val order = homeViewModelState.order
    val orderDetails = homeViewModelState.orders?.find { it.id_pedido == order?.id_pedido }?.detalles

    var showDialogQuestion by remember { mutableStateOf(false) }
    var showDialogSuccess by remember { mutableStateOf(false) }
    var showDialogError by remember { mutableStateOf(false) }
    var showDialogInfo by remember { mutableStateOf(false) }
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

            order != null -> {
                dialogMessage = "Número de pedido: ${order.id_pedido}\nDetalles: ${orderDetails ?: "N/A"}"
                showDialogInfo = true
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
                type = MARVIDialogType.INFO,
                title = "Información del pedido",
                message = dialogMessage,
                confirmButtonText = "Aceptar",
                onConfirm = {
                    showDialogInfo = false
                },
                onDismiss = {
                    showDialogInfo = false
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