// app/src/main/java/com/maqradars/ui/screens/MaqamDetailScreen.kt

package com.maqradars.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maqradars.ui.viewmodel.MaqamViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.maqradars.data.entity.Maqam
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaqamDetailScreen(
    viewModel: MaqamViewModel,
    maqamId: Long,
    onBackClick: () -> Unit
) {
    var maqam by remember { mutableStateOf<Maqam?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(maqamId) {
        coroutineScope.launch {
            maqam = viewModel.getMaqamById(maqamId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = maqam?.name ?: "Loading...") },
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
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (maqam != null) {
                Text(
                    text = maqam!!.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(text = "Maqam tidak ditemukan.")
            }
        }
    }
}