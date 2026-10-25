package com.officemitra.app.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BrandTeal,
    onPrimary = SurfaceLight,
    primaryContainer = TealLight,
    onPrimaryContainer = TealDark,
    secondary = TealDark,
    onSecondary = SurfaceLight,
    secondaryContainer = SurfaceMuted,
    onSecondaryContainer = TextPrimaryLight,
    tertiary = WarmCoral,
    onTertiary = SurfaceLight,
    tertiaryContainer = WarmSand,
    onTertiaryContainer = BrandInk,
    background = AppCanvas,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceMuted,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderLight,
    outlineVariant = BorderLight,
    error = ErrorLight,
    onError = SurfaceLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryTealDark,
    onPrimary = BackgroundDark,
    primaryContainer = SoftTealContainerDark,
    onPrimaryContainer = PrimaryTealDark,
    secondary = PrimaryTealDark,
    onSecondary = BackgroundDark,
    secondaryContainer = ElevatedSurfaceDark,
    onSecondaryContainer = PrimaryTextDark,
    tertiary = WarmCoral,
    onTertiary = BackgroundDark,
    tertiaryContainer = SoftCoralContainerDark,
    onTertiaryContainer = PrimaryTextDark,
    background = BackgroundDark,
    onBackground = PrimaryTextDark,
    surface = SurfaceDark,
    onSurface = PrimaryTextDark,
    surfaceVariant = ElevatedSurfaceDark,
    onSurfaceVariant = SecondaryTextDark,
    outline = BorderDark,
    outlineVariant = BorderDark,
    error = ErrorDark,
    onError = BackgroundDark
)

@Composable
fun OfficeMitraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
