// app/src/main/java/com/maqradars/ui/screens/MaqamDetailScreen.kt

package com.maqradars.ui.screens

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.maqradars.data.entity.AyatExample
import com.maqradars.data.entity.MaqamVariant
import com.maqradars.ui.viewmodel.MaqamViewModel
import kotlinx.coroutines.launch

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

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Pilih Varian") },
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
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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

            Divider()

            if (selectedVariant != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ayatExamples) { ayat ->
                        AyatExampleItem(
                            ayat = ayat,
                            isPlaying = currentPlayingAudio == ayat.audioPath,
                            onClick = {
                                coroutineScope.launch {
                                    if (currentPlayingAudio == ayat.audioPath) {
                                        mediaPlayer?.stop()
                                        mediaPlayer?.release()
                                        mediaPlayer = null
                                        currentPlayingAudio = null
                                    } else {
                                        mediaPlayer?.release()
                                        mediaPlayer = null
                                        val audioResourceId = context.resources.getIdentifier(ayat.audioPath.removeSuffix(".mp3"), "raw", context.packageName)
                                        if (audioResourceId != 0) {
                                            mediaPlayer = MediaPlayer.create(context, audioResourceId)
                                            mediaPlayer?.start()
                                            currentPlayingAudio = ayat.audioPath
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            } else {
                Text("Pilih varian di atas untuk melihat contoh ayat.", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

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
            Text(text = variant.variantName, style = MaterialTheme.typography.titleLarge)
            Text(text = variant.description, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Surah ${ayat.surahNumber} Ayat ${ayat.ayatNumber}", style = MaterialTheme.typography.titleLarge)
            Text(text = ayat.arabicText, style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
            if (isPlaying) {
                Text(text = "Sedang diputar...", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}