package com.marvilanundry.marvi.presentation.auth.welcome

import android.app.Activity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.marvilanundry.marvi.R
import com.marvilanundry.marvi.presentation.core.components.MARVIDialog
import com.marvilanundry.marvi.presentation.core.components.MARVIDialogType

@Composable
fun WelcomeScreen(onNavigateToLogin: () -> Unit) {
    var showDialogError by remember { mutableStateOf(false) }

    val welcomeViewModel: WelcomeViewModel = hiltViewModel()
    val welcomeViewModelState by welcomeViewModel.state.collectAsStateWithLifecycle()
    val error = welcomeViewModelState.error
    val context = LocalContext.current
    val compositionLogo by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.welcome))
    val compositionLoader by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    val animatedProgress by animateFloatAsState(
        targetValue = if (!welcomeViewModelState.isLoading) 1f else 0f,
        animationSpec = tween(3000),
        finishedListener = {
            if (welcomeViewModelState.available) onNavigateToLogin() else showDialogError = true
        })

    when {
        showDialogError -> {
            MARVIDialog(
                type = MARVIDialogType.ERROR,
                title = stringResource(id = R.string.marvi_welcome_dialog_error_title),
                message = if (error.isNullOrBlank()) stringResource(id = R.string.marvi_welcome_dialog_error_message)
                else error,
                confirmButtonText = stringResource(id = R.string.marvi_welcome_dialog_error_confirm_button),
                onConfirm = {
                    showDialogError = false
                    (context as? Activity)?.finishAffinity()
                })
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                LottieAnimation(
                    composition = compositionLogo,
                    modifier = Modifier.size(256.dp),
                    contentScale = ContentScale.Crop,
                    clipSpec = LottieClipSpec.Frame(
                        min = 0, max = 54
                    )
                )
            }
            Box(
                modifier = Modifier.weight(1f), contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = compositionLoader,
                    modifier = Modifier.size(192.dp),
                    contentScale = ContentScale.Crop,
                    iterations = LottieConstants.IterateForever
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.marvi_welcome_message),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.height(4.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outline,
                    strokeCap = StrokeCap.Round,
                    drawStopIndicator = {})
            }
        }
    }
}