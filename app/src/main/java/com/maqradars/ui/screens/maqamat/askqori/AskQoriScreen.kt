package com.maqradars.ui.screens.maqamat.askqori

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ChatMessage(val text: String, val isUser: Boolean, val timestamp: Long = System.currentTimeMillis())

private const val MIN_REQUEST_INTERVAL_MS = 1000L
private var lastRequestTime = 0L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AskQoriScreen(
    onBackClick: () -> Unit,
    chatInstance: Chat,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val applicationContext = context.applicationContext

    val messages = remember { mutableStateListOf<ChatMessage>() }
    var questionInput by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    val lazyColumnState = rememberLazyListState()
    val composableScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            composableScope.launch {
                lazyColumnState.animateScrollToItem(messages.lastIndex)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tanya Qori",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Chat Messages Area
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                state = lazyColumnState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                if (messages.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(32.dp)
                            ) {
                                Text(
                                    text = "Assalamu'alaikum! 👋",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Tanyakan kepada saya tentang Maqam, Tilawah, atau apapun yang ingin Anda ketahui tentang Al-Quran.",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                items(messages) { message ->
                    MessageBubble(message = message)
                }

                // Typing indicator
                if (isSending) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    repeat(3) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(6.dp),
                                            strokeWidth = 1.dp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Divider(
                modifier = Modifier.padding(horizontal = 0.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Input Area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = questionInput,
                    onValueChange = { questionInput = it },
                    placeholder = { Text("Ketik pertanyaan...", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp, max = 150.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    singleLine = false,
                    textStyle = androidx.compose.material3.LocalTextStyle.current.copy(fontSize = 14.sp)
                )

                FloatingActionButton(
                    onClick = {
                        val question = questionInput.trim()
                        if (question.isNotEmpty() && !isSending) {
                            messages.add(ChatMessage(question, true))
                            questionInput = ""
                            sendPrompt(
                                promptText = question,
                                messages = messages,
                                chatInstance = chatInstance,
                                setIsSending = { isSending = it },
                                applicationContext = applicationContext,
                                composableScope = composableScope
                            )
                        } else if (question.isEmpty()) {
                            Toast.makeText(applicationContext, "Pertanyaan tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .alpha(if (isSending) 0.5f else 1f),
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(percent = 50)
                ) {
                    Icon(
                        Icons.Filled.Send,
                        contentDescription = "Kirim Pesan",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .widthIn(max = 300.dp)
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) 
                    MaterialTheme.colorScheme.onPrimary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(12.dp),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}


private fun sendPrompt(
    promptText: String,
    messages: MutableList<ChatMessage>,
    chatInstance: Chat,
    setIsSending: (Boolean) -> Unit,
    applicationContext: Context,
    composableScope: CoroutineScope
) {
    // Rate limiting - tunggu minimal 1 detik dari request terakhir
    val now = System.currentTimeMillis()
    if (now - lastRequestTime < MIN_REQUEST_INTERVAL_MS) {
        val waitTime = ((MIN_REQUEST_INTERVAL_MS - (now - lastRequestTime)) / 1000) + 1
        composableScope.launch(Dispatchers.Main) {
            Toast.makeText(
                applicationContext,
                "Tunggu $waitTime detik sebelum mengirim pertanyaan lagi...",
                Toast.LENGTH_SHORT
            ).show()
        }
        return
    }
    lastRequestTime = now

    setIsSending(true)
    
    // Retry logic dengan exponential backoff - support 503 errors
    composableScope.launch(Dispatchers.IO) {
        var retryCount = 0
        val maxRetries = 5 // Lebih banyak retry untuk 503
        var success = false
        
        while (retryCount < maxRetries && !success) {
            try {
                val userContent = content { text(promptText) }
                val response = chatInstance.sendMessage(userContent)
                val generatedText = response.text

                withContext(Dispatchers.Main) {
                    if (!generatedText.isNullOrBlank()) {
                        messages.add(ChatMessage(generatedText, false))
                        success = true
                    } else {
                        messages.add(ChatMessage("Qori: Tidak ada respons dari AI.", false))
                        success = true
                    }
                }
            } catch (e: Exception) {
                retryCount++
                val isQuotaError = e.message?.contains("429") == true || e.message?.contains("Quota exceeded") == true
                val isServerError = e.message?.contains("503") == true || e.message?.contains("500") == true
                
                if ((isQuotaError || isServerError) && retryCount < maxRetries) {
                    // Exponential backoff - tunggu lebih lama sebelum retry
                    val backoffTime = (3000L * retryCount) // 3s, 6s, 9s, 12s, 15s
                    android.util.Log.d("AskQoriScreen", "Error ${if (isServerError) "503/500" else "429"}, retry #$retryCount setelah $backoffTime ms")
                    delay(backoffTime)
                } else {
                    // Final error or non-retryable error
                    withContext(Dispatchers.Main) {
                        val errorMessage = when {
                            isQuotaError -> 
                                """⏳ API quota telah habis. Silakan coba lagi dalam beberapa menit.

💡 Tips: Dapatkan API Key baru dari https://aistudio.google.com/app/apikey"""
                            isServerError ->
                                """⚠️ Server Google sedang sibuk. Mencoba lagi...

Jika terus gagal, tunggu beberapa menit dan coba lagi."""
                            e.message?.contains("403") == true -> 
                                "❌ API Key tidak valid. Periksa kembali di local.properties"
                            e.message?.contains("401") == true -> 
                                "❌ Autentikasi gagal. Periksa API Key Anda."
                            e.message?.contains("404") == true ->
                                "❌ Model tidak ditemukan. Pastikan menggunakan model yang benar."
                            else -> 
                                "❌ Error: ${e.message ?: "Terjadi kesalahan yang tidak diketahui"}"
                        }
                        messages.add(ChatMessage("Qori: $errorMessage", false))
                        android.util.Log.e("AskQoriScreen", "Send prompt final error after $retryCount retries", e)
                    }
                    success = true // Hentikan retry
                }
            }
        }

        withContext(Dispatchers.Main) {
            setIsSending(false)
        }
    }
}