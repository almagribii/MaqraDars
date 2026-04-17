package com.maqradars.ui.screens.maqamat.alquran.detailsurah

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import com.maqradars.R
import com.maqradars.data.model_api.Ayat
import com.maqradars.data.model_api.DetailSurat
import com.maqradars.ui.utils.ResponsiveUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSuratScreen(
    nomorSurat: Int,
    onBackClick: () -> Unit
) {
    var detailSurat by remember { mutableStateOf<DetailSurat?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val contentPadding = ResponsiveUtils.getContentPadding()
    val bodyFontSize = ResponsiveUtils.getBodyFontSize()

    LaunchedEffect(nomorSurat) {
        coroutineScope.launch {
            try {
                val response = ApiClient.quranApiService.getDetailSurat(nomorSurat)
                detailSurat = response.data
            } catch (_: Exception) {
                // Handle error
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = detailSurat?.namaLatin ?: "Surat",
                        fontWeight = FontWeight.Bold,
                        fontSize = ResponsiveUtils.getHeadingFontSize().value.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            modifier = Modifier.size(ResponsiveUtils.getIconSize())
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (detailSurat == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(contentPadding)
            ) {
                items(detailSurat!!.ayat) { ayat ->
                    AyatItem(ayat = ayat, contentPadding = contentPadding, fontSize = bodyFontSize)
                }
            }
        }
    }
}


@Composable
fun AyatItem(
    ayat: Ayat,
    contentPadding: androidx.compose.ui.unit.Dp = 12.dp,
    fontSize: androidx.compose.ui.unit.Dp = 14.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Top),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_noayat),
                contentDescription = "Nomor Ayat",
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "${ayat.nomorAyat}",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = ayat.teksArab,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.headlineLarge,
                fontSize = (fontSize.value + 15).sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = ayat.teksLatin,
                fontSize = (fontSize.value).sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = ayat.teksIndonesia,
                fontSize = (fontSize.value).sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = (fontSize.value + 4).sp
            )
        }
    }
}



