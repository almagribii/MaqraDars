// app/src/main/java/com/maqradars/ui/screens/SettingsScreen.kt

package com.maqradars.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Ini adalah Halaman Pengaturan", style = MaterialTheme.typography.headlineSmall)
        Text(text = "Fungsionalitas akan ditambahkan di sini.", style = MaterialTheme.typography.bodyMedium)
    }
}