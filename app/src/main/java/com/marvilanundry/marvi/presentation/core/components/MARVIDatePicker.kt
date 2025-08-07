package com.marvilanundry.marvi.presentation.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.marvilanundry.marvi.ui.theme.CustomColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MARVIDatePicker(
    onDateSelected: (Long?) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (datePickerState.selectedDateMillis != null) {
                        onDateSelected(datePickerState.selectedDateMillis)
                        onDismiss()
                    }
                }
            ) {
                Text(
                    text = "Seleccionar",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Transparent
                )
            ) {
                Text(
                    text = "Cancelar",
                    color = CustomColors.textColor
                )
            }
        },
        shape = MaterialTheme.shapes.small,
        colors = DatePickerDefaults.colors(
            containerColor = CustomColors.backgroundVariant
        )
    ) {
        DatePicker(
            state = datePickerState,
            title = null,
            headline = {
                Text(
                    text = "Selecciona una fecha",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            },
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background,
                dividerColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}