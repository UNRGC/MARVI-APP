package com.marvilanundry.marvi.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

object CustomColors {
    val backgroundVariant: Color
        @Composable
        get() = if (isSystemInDarkTheme()) MARVIBackgroundDark else MARVIBackground
    val textColor: Color
        @Composable
        get() = if (isSystemInDarkTheme()) MARVITextDark else MARVIText
    val primaryVariant: Color
        @Composable
        get() = MARVIPrimaryLight
    val secondaryVariant: Color
        @Composable
        get() = MARVISecondaryLight
    val placeholderColor: Color
        @Composable
        get() = if (isSystemInDarkTheme()) MARVIPlaceholderDark else MARVIPlaceholder
}

private val DarkColorScheme = darkColorScheme(
    primary = MARVIPrimary,
    onPrimary = MARVITextDark,
    secondary = MARVISecondary,
    onSecondary = MARVIPrimary,
    background = MARVIBackgroundDarkContent,
    outline = MARVIBorderDark,
    outlineVariant = MARVIBorderDarkLight
)

private val LightColorScheme = lightColorScheme(
    primary = MARVIPrimary,
    onPrimary = MARVITextDark,
    secondary = MARVISecondary,
    onSecondary = MARVIPrimary,
    background = MARVIBackgroundContent,
    outline = MARVIBorder,
    outlineVariant = MARVIBorderLight
)

@Composable
fun MARVITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes(
            small = RoundedCornerShape(16.dp)
        ),
        typography = Typography,
        content = content
    )
}