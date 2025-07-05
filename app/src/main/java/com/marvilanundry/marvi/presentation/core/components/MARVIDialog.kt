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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.ui.theme.CustomColors

@Composable
private fun MARVIDialogBase(
    title: String,
    message: String,
    composition: LottieComposition?,
    infinite: Boolean = false,
    dismissButtonText: String = "",
    confirmButtonText: String = "",
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CustomColors.backgroundVariant)
            .padding(32.dp, 16.dp, 32.dp, 32.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition,
                modifier = Modifier.size(128.dp),
                iterations = if (infinite) LottieConstants.IterateForever else 1,
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
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            if (confirmButtonText.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    if (dismissButtonText.isNotEmpty()) {
                        MARVISecondaryButton(
                            text = dismissButtonText,
                            modifier = Modifier.weight(1f),
                            onClick = onDismissRequest
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    MARVIPrimaryButton(
                        text = confirmButtonText,
                        modifier = Modifier.weight(1f),
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}

@Composable
fun MARVIDialogLoading(
    visible: Boolean, title: String, message: String
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))

    if (visible) {
        Dialog(
            onDismissRequest = {}, properties = DialogProperties(
                dismissOnBackPress = false, dismissOnClickOutside = false
            )
        ) {
            MARVIDialogBase(
                title = title, message = message, composition = composition, infinite = true
            )
        }
    }
}

@Composable
fun MARVIDialogError(
    visible: Boolean,
    title: String,
    message: String,
    confirmButtonText: String,
    onConfirm: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))

    if (visible) {
        Dialog(
            onDismissRequest = onConfirm, properties = DialogProperties(
                dismissOnBackPress = true, dismissOnClickOutside = true
            )
        ) {
            MARVIDialogBase(
                title = title,
                message = message,
                composition = composition,
                confirmButtonText = confirmButtonText,
                onConfirm = onConfirm
            )
        }
    }
}

@Composable
fun MARVIDialogSuccess(
    visible: Boolean,
    title: String,
    message: String,
    confirmButtonText: String,
    onConfirm: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))

    if (visible) {
        Dialog(
            onDismissRequest = onConfirm, properties = DialogProperties(
                dismissOnBackPress = true, dismissOnClickOutside = true
            )
        ) {
            MARVIDialogBase(
                title = title,
                message = message,
                composition = composition,
                confirmButtonText = confirmButtonText,
                onConfirm = onConfirm
            )
        }
    }
}

@Composable
fun MARVIDialogQuestion(
    visible: Boolean,
    title: String,
    message: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.question))

    if (visible) {
        Dialog(
            onDismissRequest = onDismissRequest, properties = DialogProperties(
                dismissOnBackPress = true, dismissOnClickOutside = true
            )
        ) {
            MARVIDialogBase(
                title = title,
                message = message,
                composition = composition,
                dismissButtonText = dismissButtonText,
                confirmButtonText = confirmButtonText,
                onDismissRequest = onDismissRequest,
                onConfirm = onConfirm
            )
        }
    }
}