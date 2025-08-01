// app/src/main/java/com/maqradars/ui/screens/RecitationTypeSelectionScreen.kt

package com.maqradars.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecitationTypeSelectionScreen(
    maqamId: Long,
    maqamName: String,
    onMujawwadClick: (Long) -> Unit,
    onTilawahClick: (String) -> Unit, // Mengubah tipe parameter
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Pilih Bacaan: $maqamName") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Pilih Jenis Bacaan", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { onMujawwadClick(maqamId) },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(text = "Mujawwad", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onTilawahClick("Al-Fatihah")
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(text = "Tilawah", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}