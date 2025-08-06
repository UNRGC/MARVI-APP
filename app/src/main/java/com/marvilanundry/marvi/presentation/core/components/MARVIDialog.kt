package com.marvilanundry.marvi.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.ui.theme.CustomColors

@Composable
private fun MARVIDialogBase(
    title: String,
    message: String,
    textAlign: TextAlign,
    composition: LottieComposition?,
    infinite: Boolean,
    dismissButtonText: String,
    confirmButtonText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(CustomColors.backgroundVariant)
            .padding(32.dp, 16.dp, 32.dp, 32.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition,
                modifier = Modifier.size(128.dp),
                contentScale = ContentScale.Crop,
                iterations = if (infinite) LottieConstants.IterateForever else 1,
                clipSpec = if (!infinite) LottieClipSpec.Frame(
                    min = 24, max = 90
                ) else null
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = CustomColors.textColor,
                textAlign = textAlign,
                modifier = Modifier.fillMaxWidth()
            )
            if (confirmButtonText.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    if (dismissButtonText.isNotEmpty()) {
                        MARVIButton(
                            text = dismissButtonText,
                            modifier = Modifier.weight(1f),
                            type = MARVIButtonType.SECONDARY,
                            onClick = onDismiss
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    MARVIButton(
                        text = confirmButtonText,
                        modifier = Modifier.weight(1f),
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}

enum class MARVIDialogType(val lottieRes: Int, val infinite: Boolean) {
    LOADING(R.raw.loader, true), ERROR(R.raw.error, false), SUCCESS(R.raw.success, false), QUESTION(
        R.raw.question, false
    )
}

@Composable
fun MARVIDialog(
    type: MARVIDialogType,
    title: String,
    message: String,
    textAlign: TextAlign = TextAlign.Center,
    confirmButtonText: String = "",
    dismissButtonText: String = "",
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(type.lottieRes)
    )

    Dialog(
        onDismissRequest = if (type == MARVIDialogType.LOADING) ({}) else onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = type != MARVIDialogType.LOADING,
            dismissOnClickOutside = type != MARVIDialogType.LOADING
        )
    ) {
        MARVIDialogBase(
            title = title,
            message = message,
            textAlign = textAlign,
            composition = composition,
            infinite = type.infinite,
            confirmButtonText = confirmButtonText,
            dismissButtonText = dismissButtonText,
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
}