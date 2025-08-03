// app/src/main/java/com/maqradars/ui/screens/AskQoriScreen.kt

package com.maqradars.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ChatMessage(val text: String, val isUser: Boolean)

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
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .padding(bottom = 140.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = questionInput,
                    onValueChange = { questionInput = it },
                    placeholder = { Text("Tanya Qori...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 50.dp, max = 150.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

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
                    modifier = Modifier.size(50.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(percent = 50)
                ) {
                    Icon(
                        Icons.Filled.Send,
                        contentDescription = "Kirim Pesan",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                state = lazyColumnState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(message = message)
                }
            }

            if (isSending) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Qori sedang mengetik...",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .widthIn(max = 280.dp)
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(12.dp)
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
    setIsSending(true)
    composableScope.launch(Dispatchers.IO) {
        try {
            val userContent = content { text(promptText) }
            val response = chatInstance.sendMessage(userContent)
            val generatedText = response.text

            withContext(Dispatchers.Main) {
                if (generatedText != null) {
                    messages.add(ChatMessage(generatedText, false))
                } else {
                    messages.add(ChatMessage("Qori: Tidak ada respons dari AI.", false))
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                messages.add(ChatMessage("Qori: Terjadi kesalahan: ${e.message}", false))
            }
        } finally {
            withContext(Dispatchers.Main) {
                setIsSending(false)
            }
        }
    }
}