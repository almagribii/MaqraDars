package com.maqradars.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maqradars.data.model_api.Surat
import kotlinx.coroutines.launch

// Pastikan kamu mengimpor semua yang dibutuhkan
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarSuratScreen(
    onSuratClick: (Int) -> Unit,
    onBackClick: () -> Unit // Tambahkan ini
) {
    var daftarSurat by remember { mutableStateOf<List<Surat>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = ApiClient.quranApiService.getDaftarSurat()
                daftarSurat = response.data
            } catch (e: Exception) {
                // Tangani error
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Surat", fontWeight = FontWeight.Bold) },
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
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(daftarSurat) { surat ->
                SuratItem(surat = surat, onSuratClick = onSuratClick)
            }
        }
    }
}

@Composable
fun SuratItem(surat: Surat, onSuratClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSuratClick(surat.nomor) } // DI SINI! Kita mengirimkan 'surat.nomor' yang bertipe Int
            .padding(16.dp)
            .clickable { onSuratClick(surat.nomor) }
    ) {
        Text(text = "${surat.nomor}. ${surat.namaLatin}")
        Text(text = surat.nama)
    }
}