// app/src/main/java/com/maqradars/ui/screens/TilawahScreen.kt

package com.maqradars.ui.screens

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TilawahScreen(onBackClick: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val audioResourceId = context.resources.getIdentifier("alfatihah_full", "raw", context.packageName)
        if (audioResourceId != 0) {
            mediaPlayer = MediaPlayer.create(context, audioResourceId)
        }

        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tilawah Al-Fatihah") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Memutar Surah Al-Fatihah", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = {
                    mediaPlayer?.apply {
                        if (isPlaying) {
                            pause()
                        } else {
                            start()
                        }
                        isPlaying = !isPlaying
                    }
                }) {
                    Text(if (isPlaying) "Jeda" else "Putar")
                }
                Button(onClick = {
                    mediaPlayer?.apply {
                        stop()
                        prepareAsync()
                        isPlaying = false
                    }
                }) {
                    Text("Hentikan")
                }
            }
        }
    }
}