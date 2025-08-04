package com.maqradars.ui.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.maqradars.data.model_api.Ayat
import com.maqradars.data.model_api.DetailSurat
import com.maqradars.data.model_api.QuranApiService
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue






import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight

// ... import lainnya

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSuratScreen(
    nomorSurat: Int,
    onBackClick: () -> Unit // Tambahkan ini
) {
    var detailSurat by remember { mutableStateOf<DetailSurat?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(nomorSurat) {
        coroutineScope.launch {
            try {
                val response = ApiClient.quranApiService.getDetailSurat(nomorSurat)
                detailSurat = response.data
            } catch (e: Exception) {
                // Tangani error
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Tampilkan nama surat setelah data berhasil dimuat
                    Text(
                        text = detailSurat?.namaLatin ?: "Surat",
                        fontWeight = FontWeight.Bold
                    )
                },
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
        if (detailSurat == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(contentPadding = innerPadding, modifier = Modifier.fillMaxSize()) {
                items(detailSurat!!.ayat) { ayat ->
                    AyatItem(ayat = ayat)
                }
            }
        }
    }
}

@Composable
fun AyatItem(ayat: Ayat) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // PERBAIKAN DI SINI
        // Menggunakan nama properti yang baru
        Text(
            text = ayat.teksArab,
            textAlign = TextAlign.End,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )
        // ...
        Text(
            // Menggunakan nama properti yang baru
            text = "${ayat.nomorAyat}. ${ayat.teksIndonesia}",
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}