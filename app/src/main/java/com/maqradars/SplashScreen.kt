// app/src/main/java/com/maqradars/ui/screens/SplashScreen.kt

package com.maqradars.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maqradars.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // State animasi untuk alpha (transparansi) dan skala
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(key1 = true) {
        // Animasi Fading In dan Scaling Up
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000) // Animasi 1 detik
            )
        }
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000) // Animasi 1 detik
            )
        }

        delay(4000) // Tunda total selama 3 detik sebelum berpindah layar
        onTimeout()
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = Color(0xFFA5D6A7)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_maqradars),
            contentDescription = "Logo MaqraDars",
            // Terapkan modifier animasi
            modifier = Modifier
                .size(150.dp)
                .alpha(alpha.value) // Menerapkan animasi alpha
                .scale(scale.value) // Menerapkan animasi skala
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "MaqraDars",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.alpha(alpha.value) // Teks juga ikut dianimasikan
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Belajar Maqam Al-Qur'an",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.alpha(alpha.value)
        )
    }
}