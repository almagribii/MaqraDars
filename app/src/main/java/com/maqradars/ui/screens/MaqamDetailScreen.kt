// app/src/main/java/com/maqradars/ui/screens/MaqamDetailScreen.kt

package com.maqradars.ui.screens

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.maqradars.data.entity.AyatExample
import com.maqradars.data.entity.MaqamVariant
import com.maqradars.ui.viewmodel.MaqamViewModel
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Stop
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.selection.SelectionContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaqamDetailScreen(
    viewModel: MaqamViewModel,
    maqamId: Long,
    onBackClick: () -> Unit
) {
    val variants by viewModel.getVariantsByMaqamId(maqamId).collectAsState(initial = emptyList())
    var selectedVariant by remember { mutableStateOf<MaqamVariant?>(null) }

    val ayatExamples by if (selectedVariant != null) {
        viewModel.getAyatExamplesByMaqamVariantId(selectedVariant!!.id).collectAsState(initial = emptyList())
    } else {
        remember { mutableStateOf(emptyList()) }
    }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var currentPlayingAudio by remember { mutableStateOf<String?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = selectedVariant?.variantName ?: "Pilih Varian", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (currentPlayingAudio != null) {
                AudioPlayerBar(
                    isPlaying = isPlaying,
                    onPlayPauseClick = {
                        if (isPlaying) {
                            mediaPlayer?.pause()
                        } else {
                            mediaPlayer?.start()
                        }
                        isPlaying = !isPlaying
                    },
                    onStopClick = {
                        mediaPlayer?.apply {
                            stop()
                            prepareAsync()
                        }
                        isPlaying = false
                        currentPlayingAudio = null
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            // Deskripsi Varian yang Dipilih
            selectedVariant?.let { variant ->
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = variant.variantName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = variant.description, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                }
            }

            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            // Daftar Varian Maqam
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(variants) { variant ->
                    MaqamVariantItem(
                        variant = variant,
                        isSelected = selectedVariant?.id == variant.id,
                        onClick = {
                            selectedVariant = variant
                        }
                    )
                }
            }

            Divider(modifier = Modifier.padding(top = 8.dp))

            // Daftar Ayat untuk Varian yang Dipilih
            if (selectedVariant != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(ayatExamples) { ayat ->
                        AyatExampleItem(
                            ayat = ayat,
                            isPlaying = currentPlayingAudio == ayat.audioPath,
                            onClick = {
                                coroutineScope.launch {
                                    if (currentPlayingAudio == ayat.audioPath && isPlaying) {
                                        mediaPlayer?.pause()
                                        isPlaying = false
                                    } else {
                                        mediaPlayer?.release()
                                        mediaPlayer = null
                                        val audioResourceId = context.resources.getIdentifier(ayat.audioPath.removeSuffix(".mp3"), "raw", context.packageName)
                                        if (audioResourceId != 0) {
                                            mediaPlayer = MediaPlayer.create(context, audioResourceId)
                                            mediaPlayer?.start()
                                            currentPlayingAudio = ayat.audioPath
                                            isPlaying = true
                                        } else {
                                            Toast.makeText(context, "File audio tidak ditemukan!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Pilih varian di atas untuk melihat contoh ayat.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MaqamVariantItem(variant: MaqamVariant, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(modifier = Modifier.basicMarquee(),
                text = variant.variantName,
                style = MaterialTheme.typography.titleLarge, maxLines = 1,
                )
            Text(text = variant.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun AyatExampleItem(ayat: AyatExample, isPlaying: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isPlaying) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ayat.ayatNumber.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Surah ${ayat.surahNumber} Ayat ${ayat.ayatNumber}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                SelectionContainer { // Membuat teks bisa disalin
                    Text(text = ayat.arabicText, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Icon(
                imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                contentDescription = if (isPlaying) "Jeda" else "Putar",
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AudioPlayerBar(
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onStopClick: () -> Unit
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPlayPauseClick) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = "Putar/Jeda",
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(onClick = onStopClick) {
                Icon(
                    imageVector = Icons.Filled.Stop,
                    contentDescription = "Hentikan",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}