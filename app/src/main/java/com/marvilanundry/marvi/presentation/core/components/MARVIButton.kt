package com.marvilanundry.marvi.presentation.core.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.ui.theme.CustomColors

@Composable
private fun MARVIButtonBase(
    text: String,
    modifier: Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    enabled: Boolean = true,
    outlined: Boolean = false,
    message: String = "",
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Box(modifier = modifier) {
        if (outlined) {
            OutlinedButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(text = text)
            }
        } else {
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor, contentColor = contentColor
                )
            ) {
                Text(text = text)
            }
        }

        if (!enabled && message.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    })
        }
    }
}

@Composable
fun MARVIPrimaryButton(
    text: String,
    modifier: Modifier,
    message: String = "",
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    MARVIButtonBase(
        text = text,
        modifier = modifier,
        enabled = enabled,
        outlined = false,
        message = message,
        onClick = onClick
    )
}

@Composable
fun MARVIPrimaryOutlinedButton(
    text: String, modifier: Modifier, enabled: Boolean = true, onClick: () -> Unit
) {
    MARVIButtonBase(
        text = text,
        modifier = modifier,
        enabled = enabled,
        outlined = true,
        message = "",
        onClick = onClick
    )
}

@Composable
fun MARVISecondaryButton(
    text: String, modifier: Modifier, enabled: Boolean = true, onClick: () -> Unit
) {
    MARVIButtonBase(
        text = text,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.outline,
        contentColor = CustomColors.textColor,
        enabled = enabled,
        outlined = false,
        message = "",
        onClick = onClick
    )
}

@Composable
fun MARVIBackIconButton(
    enabled: Boolean = true, onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(40.dp),
        enabled = enabled,
        contentPadding = PaddingValues(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.outline,
            contentColor = CustomColors.textColor
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_left_short),
            contentDescription = "Botón de regreso",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun MARVILinkButton(
    text: String,
    modifier: Modifier,
    fontSize: TextUnit,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val registerInteraction = remember { MutableInteractionSource() }
    val isPressedRegister = registerInteraction.collectIsPressedAsState().value

    val color = when {
        !enabled -> MaterialTheme.colorScheme.outline
        isPressedRegister -> CustomColors.primaryVariant
        else -> MaterialTheme.colorScheme.primary
    }

    Text(
        text = text,
        modifier = modifier.clickable(
            enabled = enabled, interactionSource = registerInteraction, indication = null
        ) {
            onClick()
        },
        color = color,
        fontSize = fontSize,
        textDecoration = if (isPressedRegister) TextDecoration.Underline else TextDecoration.None,
        textAlign = TextAlign.Center
    )
}