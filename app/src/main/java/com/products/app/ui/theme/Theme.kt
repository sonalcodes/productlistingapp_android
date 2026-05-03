package com.products.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryWarm,
    onPrimary = Color.Black,
    primaryContainer = SurfaceDark,
    onPrimaryContainer = TextPrimaryLight,
    secondary = AccentTan,
    onSecondary = OnAccentDark,
    tertiary = AccentTan,
    background = BackgroundDark,
    onBackground = TextPrimaryLight,
    surface = SurfaceDark,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceDark,
    onSurfaceVariant = TextSecondaryMuted,
    error = ErrorRose,
    onError = Color.Black,
)

@Composable
fun ProductAppTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = typographyWithDmSans(),
        content = content,
    )
}
