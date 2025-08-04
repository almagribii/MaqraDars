package com.maqradars.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

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
            LazyColumn(contentPadding = innerPadding, modifier = Modifier.fillMaxSize().padding(bottom = 120.dp)) {
                items(detailSurat!!.ayat) { ayat ->
                    AyatItem(ayat = ayat)
                }
            }
        }
    }
}


@Composable
fun AyatItem(ayat: Ayat) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.Top)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${ayat.nomorAyat}",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text(
                    text = ayat.teksArab,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = ayat.teksLatin,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = ayat.teksIndonesia,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

}