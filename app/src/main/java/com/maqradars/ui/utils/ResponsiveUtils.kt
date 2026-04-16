package com.maqradars.ui.utils


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Utility object untuk responsive design
 * Mendukung berbagai ukuran screen: compact, medium, expanded
 */
object ResponsiveUtils {
    
    /**
     * Enum untuk klasifikasi ukuran screen
     */
    enum class ScreenSize {
        COMPACT,      // Phone portrait (< 600 dp)
        MEDIUM,       // Tablet portrait atau phone landscape (600-840 dp)
        EXPANDED      // Tablet landscape (> 840 dp)
    }

    /**
     * Mendapatkan ukuran screen berdasarkan width
     */
    @Composable
    fun getScreenSize(): ScreenSize {
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp

        return when {
            screenWidthDp < 600 -> ScreenSize.COMPACT
            screenWidthDp < 840 -> ScreenSize.MEDIUM
            else -> ScreenSize.EXPANDED
        }
    }

    /**
     * Padding responsif untuk konten utama
     */
    @Composable
    fun getContentPadding(): Dp {
        return when (getScreenSize()) {
            ScreenSize.COMPACT -> 12.dp
            ScreenSize.MEDIUM -> 16.dp
            ScreenSize.EXPANDED -> 24.dp
        }
    }

    /**
     * Padding responsif untuk card/item
     */
    @Composable
    fun getItemPadding(): Dp {
        return when (getScreenSize()) {
            ScreenSize.COMPACT -> 8.dp
            ScreenSize.MEDIUM -> 12.dp
            ScreenSize.EXPANDED -> 16.dp
        }
    }

    /**
     * Font size responsif untuk heading
     */
    @Composable
    fun getHeadingFontSize(): Dp {
        return when (getScreenSize()) {
            ScreenSize.COMPACT -> 20.dp
            ScreenSize.MEDIUM -> 24.dp
            ScreenSize.EXPANDED -> 28.dp
        }
    }

    /**
     * Font size responsif untuk body text
     */
    @Composable
    fun getBodyFontSize(): Dp {
        return when (getScreenSize()) {
            ScreenSize.COMPACT -> 14.dp
            ScreenSize.MEDIUM -> 16.dp
            ScreenSize.EXPANDED -> 18.dp
        }
    }

    /**
     * Jumlah kolom responsif untuk grid
     */
    @Composable
    fun getGridColumns(): Int {
        return when (getScreenSize()) {
            ScreenSize.COMPACT -> 3
            ScreenSize.MEDIUM -> 4
            ScreenSize.EXPANDED -> 6
        }
    }

    /**
     * Height banner responsif
     */
    @Composable
    fun getBannerHeight(): Dp {
        return when (getScreenSize()) {
            ScreenSize.COMPACT -> 180.dp
            ScreenSize.MEDIUM -> 210.dp
            ScreenSize.EXPANDED -> 250.dp
        }
    }

    /**
     * Icon size responsif
     */
    @Composable
    fun getIconSize(): Dp {
        return when (getScreenSize()) {
            ScreenSize.COMPACT -> 20.dp
            ScreenSize.MEDIUM -> 24.dp
            ScreenSize.EXPANDED -> 28.dp
        }
    }

    /**
     * FAB size responsif
     */
    @Composable
    fun getFabSize(): Dp {
        return when (getScreenSize()) {
            ScreenSize.COMPACT -> 56.dp
            ScreenSize.MEDIUM -> 64.dp
            ScreenSize.EXPANDED -> 72.dp
        }
    }
}

