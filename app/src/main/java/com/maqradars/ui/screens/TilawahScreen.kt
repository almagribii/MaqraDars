// app/src/main/java/com/maqradars/ui/screens/TilawahScreen.kt

package com.maqradars.ui.screens

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TilawahScreen(surahName: String, onBackClick: () -> Unit) {
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val audioResourceId = context.resources.getIdentifier("alfatihah_full", "raw", context.packageName)
        if (audioResourceId != 0) {
            mediaPlayer = MediaPlayer.create(context, audioResourceId)
            mediaPlayer?.setOnCompletionListener {
                isPlaying = false
            }
        } else {
            Toast.makeText(context, "File audio tidak ditemukan!", Toast.LENGTH_SHORT).show()
        }

        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tilawah $surahName") },
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.alfatihah ), // <-- Ganti dengan nama file Anda
                contentDescription = "Teks Surah Al-Fatihah",
                modifier = Modifier.padding(16.dp)
            )

            Text(text = "Memutar Surah $surahName", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(onClick = {
                    mediaPlayer?.apply {
                        if (isPlaying) {
                            pause()
                        } else {
                            start()
                        }
                        isPlaying = !isPlaying
                    }
                }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Jeda" else "Putar",
                        modifier = Modifier.size(48.dp)
                    )
                }
                IconButton(onClick = {
                    mediaPlayer?.apply {
                        stop()
                        prepareAsync()
                        isPlaying = false
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Stop,
                        contentDescription = "Hentikan",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}