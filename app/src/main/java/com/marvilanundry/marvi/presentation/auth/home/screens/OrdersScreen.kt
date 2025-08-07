package com.marvilanundry.marvi.presentation.auth.home.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.presentation.auth.home.HomeUiState
import com.marvilanundry.marvi.presentation.auth.home.HomeViewModel
import com.marvilanundry.marvi.presentation.auth.home.components.ClearableTextField
import com.marvilanundry.marvi.presentation.auth.home.components.Container
import com.marvilanundry.marvi.presentation.auth.home.components.SectionHeader
import com.marvilanundry.marvi.presentation.core.components.MARVIButton
import com.marvilanundry.marvi.presentation.core.components.MARVIButtonType
import com.marvilanundry.marvi.ui.theme.CustomColors
import java.util.Locale

@Composable
fun OrdersScreen(homeViewModel: HomeViewModel, homeViewModelState: HomeUiState) {
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
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                homeViewModel.getOrder(order.id_pedido)
                            }
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.small
                            )
                            .background(
                                color = CustomColors.backgroundVariant,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(16.dp),
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
                                        text = "$${String.format(Locale.getDefault(), "%.2f", order.total)}",
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