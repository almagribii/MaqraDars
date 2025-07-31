// app/src/main/java/com/maqradars/ui/theme/Theme.kt

package com.maqradars.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Buat satu ColorScheme sederhana dari palet warna kita
private val MaqraDarsColorScheme = ColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = Primary,
    onPrimaryContainer = OnPrimary,
    inversePrimary = OnBackground,
    secondary = Primary,
    onSecondary = OnPrimary,
    secondaryContainer = Primary,
    onSecondaryContainer = OnPrimary,
    tertiary = Primary,
    onTertiary = OnPrimary,
    tertiaryContainer = Primary,
    onTertiaryContainer = OnPrimary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = Surface,
    onSurfaceVariant = OnSurface,
    surfaceTint = Primary,
    inverseSurface = OnSurface,
    inverseOnSurface = Background,
    error = Color(0xFFCF6679), // Warna Error
    onError = Color.Black,
    errorContainer = Color(0xFFCF6679),
    onErrorContainer = Color.Black,
    outline = Primary,
    outlineVariant = Primary,
    scrim = Color.Black,
    // Beberapa versi Material 3 mungkin membutuhkan argumen tambahan seperti ini
    // surfaceBright = Surface,
    // surfaceDim = Surface,
    // surfaceContainer = Surface,
    // surfaceContainerHigh = Surface,
    // surfaceContainerHighest = Surface,
    // surfaceContainerLow = Surface,
    // surfaceContainerLowest = Surface,
)

@Composable
fun MaqraDarsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MaqraDarsColorScheme,
        typography = Typography,
        content = content
    )
}