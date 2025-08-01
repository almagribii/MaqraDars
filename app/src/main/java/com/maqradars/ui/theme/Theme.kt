// app/src/main/java/com/maqradars/ui/theme/Theme.kt

package com.maqradars.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = dark_theme_primary,
    onPrimary = dark_theme_onPrimary,
    primaryContainer = dark_theme_primaryContainer,
    onPrimaryContainer = dark_theme_onPrimaryContainer,
    secondary = dark_theme_secondary,
    onSecondary = dark_theme_onSecondary,
    secondaryContainer = dark_theme_secondaryContainer,
    onSecondaryContainer = dark_theme_onSecondaryContainer,
    tertiary = dark_theme_tertiary,
    onTertiary = dark_theme_onTertiary,
    tertiaryContainer = dark_theme_tertiaryContainer,
    onTertiaryContainer = dark_theme_onTertiaryContainer,
    background = dark_theme_background,
    onBackground = dark_theme_onBackground,
    surface = dark_theme_surface,
    onSurface = dark_theme_onSurface,
    surfaceVariant = dark_theme_surfaceVariant,
    onSurfaceVariant = dark_theme_onSurfaceVariant,
)

private val LightColorScheme = lightColorScheme(
    primary = light_theme_primary,
    onPrimary = light_theme_onPrimary,
    primaryContainer = light_theme_primaryContainer,
    onPrimaryContainer = light_theme_onPrimaryContainer,
    secondary = light_theme_secondary,
    onSecondary = light_theme_onSecondary,
    secondaryContainer = light_theme_secondaryContainer,
    onSecondaryContainer = light_theme_onSecondaryContainer,
    tertiary = light_theme_tertiary,
    onTertiary = light_theme_onTertiary,
    tertiaryContainer = light_theme_tertiaryContainer,
    onTertiaryContainer = light_theme_onTertiaryContainer,
    background = light_theme_background,
    onBackground = light_theme_onBackground,
    surface = light_theme_surface,
    onSurface = light_theme_onSurface,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
)

@Composable
fun MaqraDarsTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}