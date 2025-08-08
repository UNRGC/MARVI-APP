package com.marvilanundry.marvi.presentation.auth.home.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.presentation.core.components.MARVITextField
import com.marvilanundry.marvi.ui.theme.CustomColors

// Contenedor con bordes y fondo personalizado
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

// Columna con scroll vertical, bordes y fondo personalizado
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

// Encabezado de sección con icono y título
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

// Campo de texto con icono para limpiar el contenido
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
        trailingIcon = if (value.isNotBlank()) {
            {
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
            }
        } else {
            null
        },
        keyboardType = keyboardType,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLength = maxLength,
        onValueChange = onValueChange
    )
}