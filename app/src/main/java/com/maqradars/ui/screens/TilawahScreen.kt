// app/src/main/java/com/maqradars/ui/screens/TilawahScreen.kt

package com.maqradars.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TilawahScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Tilawah Al-Fatihah") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Memutar Surah Al-Fatihah...", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))
            // TODO: Tambahkan kontrol audio di sini
        }
    }
}