// app/src/main/java/com/maqradars/ui/screens/RecitationTypeSelectionScreen.kt

package com.maqradars.ui.screens.maqamat.list.maqam

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import com.maqradars.R
import kotlinx.coroutines.launch
import java.util.Locale

fun formatTime(milliseconds: Int): String {
    val seconds = milliseconds / 1000
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format(Locale.US, "%02d:%02d", minutes, secs)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecitationTypeSelectionScreen(
    maqamId: Long,
    maqamName: String,
    onBackClick: () -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Tilawah", "Mujawwad")

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0) }
    var duration by remember { mutableStateOf(0) }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            mediaPlayer?.let {
                currentPosition = it.currentPosition
                duration = it.duration
            }
            kotlinx.coroutines.delay(100)
        }
    }

    LaunchedEffect(selectedTabIndex) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
        currentPosition = 0
        duration = 0
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = maqamName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))

                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.95f),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                val imageResourceId = if (selectedTabIndex == 0) {
                    R.drawable.alfatihah
                } else {
                    val imageName = when {
                        maqamName.equals("Bayati", ignoreCase = true) -> "bayati_mujawwad"
                        maqamName.equals("Hijaz", ignoreCase = true) -> "hijaz_mujawwad"
                        maqamName.equals("Jiharkah", ignoreCase = true) -> "jiharkah_mujawwad"
                        maqamName.equals("Rast", ignoreCase = true) -> "rast_mujawwad"
                        maqamName.equals("Sika", ignoreCase = true) -> "sika_mujawwad"
                        maqamName.equals("Nahawand", ignoreCase = true) -> "nahawand_mujawwad"
                        maqamName.equals("Soba", ignoreCase = true) || maqamName.equals("Shoba", ignoreCase = true) -> "soba_mujawwad"
                        else -> "mujawwad_$maqamId"
                    }
                    @Suppress("DiscouragedApi")
                    val id = context.resources.getIdentifier(imageName, "drawable", context.packageName)
                    if (id != 0) id else R.drawable.alfatihah
                }

                Image(
                    painter = painterResource(id = imageResourceId),
                    contentDescription = "Surah $maqamName",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                    
                    if (duration > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(currentPosition.toFloat() / duration.toFloat())
                                .height(3.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                        
                        Box(
                            modifier = Modifier
                                .offset(
                                    x = ((currentPosition.toFloat() / duration.toFloat()) * 100).dp - 6.dp
                                )
                                .size(12.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(50)
                                )
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatTime(currentPosition),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (isPlaying) {
                                    mediaPlayer?.pause()
                                    isPlaying = false
                                } else {
                                    if (mediaPlayer == null) {
                                        mediaPlayer?.release()
                                        mediaPlayer = null
                                        
                                        val audioPath = when {
                                            selectedTabIndex == 0 && maqamName.equals("Rast", ignoreCase = true) -> "rast"
                                            selectedTabIndex == 0 && maqamName.equals("Jiharkah", ignoreCase = true) -> "jiharkah"
                                            selectedTabIndex == 0 && maqamName.equals("Nahawand", ignoreCase = true) -> "nahawand"
                                            selectedTabIndex == 0 -> "tilawah_$maqamId"
                                            else -> "mujawwad_$maqamId"
                                        }
                                        
                                        @Suppress("DiscouragedApi")
                                        val audioResourceId = context.resources.getIdentifier(audioPath, "raw", context.packageName)
                                        
                                        if (audioResourceId != 0) {
                                            mediaPlayer = MediaPlayer.create(context, audioResourceId)
                                            mediaPlayer?.start()
                                            isPlaying = true
                                        } else {
                                            Toast.makeText(context, "File audio tidak ditemukan: $audioPath", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        mediaPlayer?.start()
                                        isPlaying = true
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(50)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isPlaying) "Jeda" else "Putar",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    
                    Text(
                        text = formatTime(duration),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    title,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                if (selectedTabIndex == index) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                shape = RoundedCornerShape(50)
                                            )
                                    )
                                }
                            }
                        },
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}

