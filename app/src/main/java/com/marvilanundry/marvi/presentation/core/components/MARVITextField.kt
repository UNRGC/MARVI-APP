package com.marvilanundry.marvi.presentation.core.components

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.marvilanundry.marvi.ui.theme.CustomColors

@Composable
private fun MARVITextFieldBase(
    labelContent: @Composable () -> Unit,
    modifier: Modifier,
    value: String,
    readOnly: Boolean,
    placeholder: String,
    trailingIcon: @Composable (() -> Unit)?,
    visualTransformation: VisualTransformation,
    keyboardType: KeyboardType,
    keyboardActions: KeyboardActions,
    capitalization: KeyboardCapitalization,
    singleLine: Boolean,
    maxLength: Int,
    onValueChange: (String) -> Unit
) {
    val registerInteraction = remember { MutableInteractionSource() }
    val isFocused by registerInteraction.collectIsFocusedAsState()

    Column(modifier = modifier) {
        labelContent()
        TextField(
            value = value,
            onValueChange = { if (it.length <= maxLength) onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (isFocused) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.small
                ),
            readOnly = readOnly,
            placeholder = {
                Text(text = placeholder, color = CustomColors.placeholderColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
            },
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                capitalization = capitalization
            ),
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            interactionSource = registerInteraction,
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = CustomColors.backgroundVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedTextColor = CustomColors.textColor,
                unfocusedTextColor = CustomColors.textColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun MARVITextField(
    label: String? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    value: String,
    readOnly: Boolean = false,
    placeholder: String,
    icon: Int? = null,
    iconDescription: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    singleLine: Boolean,
    maxLength: Int = 100,
    onValueChange: (String) -> Unit
) {
    MARVITextFieldBase(
        labelContent = {
            if (label != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (icon != null && iconDescription != null) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = iconDescription,
                            tint = CustomColors.textColor
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    Text(text = label, color = CustomColors.textColor)
                }
                Spacer(modifier = Modifier.height(12.dp))
            } else null
        },
        modifier = modifier,
        value = value,
        readOnly = readOnly,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardType = keyboardType,
        keyboardActions = keyboardActions,
        capitalization = capitalization,
        singleLine = singleLine,
        maxLength = maxLength,
        onValueChange = onValueChange
    )
}