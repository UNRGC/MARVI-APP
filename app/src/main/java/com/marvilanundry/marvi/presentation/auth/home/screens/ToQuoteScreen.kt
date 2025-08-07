package com.marvilanundry.marvi.presentation.auth.home.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.presentation.auth.home.HomeUiState
import com.marvilanundry.marvi.presentation.auth.home.HomeViewModel
import com.marvilanundry.marvi.presentation.auth.home.components.ClearableTextField
import com.marvilanundry.marvi.presentation.auth.home.components.ColumnScrollable
import com.marvilanundry.marvi.presentation.auth.home.components.Container
import com.marvilanundry.marvi.presentation.auth.home.components.SectionHeader

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
            if (homeViewModelState.services != null && homeViewModelState.services.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(homeViewModelState.services.size) { index ->
                        val service = homeViewModelState.services[index]
                        FilterChip(
                            selected = homeViewModelState.service == index,
                            onClick = {
                                homeViewModel.onToQuoteChange("")
                                homeViewModel.setService(index)
                            },
                            label = {
                                Text(
                                    text = "${service.nombre} (${service.nombre_unidad})",
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
            } else {
                Text(
                    text = "No hay servicios disponibles"
                )
            }
            ClearableTextField(
                label = "Ingresa la cantidad",
                value = homeViewModelState.toQuoteInput,
                placeholder = "Ejemplo: 1234",
                keyboardType = KeyboardType.Number,
                onValueChange = { homeViewModel.onToQuoteChange(it) },
            )
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
                        text = "$${homeViewModelState.toQuote}",
                        color = MaterialTheme.colorScheme.onPrimary
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