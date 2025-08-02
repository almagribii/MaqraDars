// app/src/main/java/com/maqradars/ui/screens/PrivacyPolicyScreen.kt

package com.maqradars.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kebijakan Privasi") },
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Kebijakan Privasi Aplikasi MaqraDars",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Aplikasi MaqraDars (“Aplikasi”) dibuat untuk memberikan panduan dalam mempelajari Maqam Al-Qur'an secara offline. Kami sangat menghargai privasi Anda dan ingin memastikan Anda merasa aman saat menggunakan Aplikasi kami.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "1. Pengumpulan Data Pribadi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Aplikasi MaqraDars tidak mengumpulkan data pribadi apa pun dari pengguna, seperti nama, email, lokasi, atau informasi identitas lainnya. Aplikasi ini tidak memiliki fitur pendaftaran atau login.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "2. Penyimpanan Data",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Semua data yang digunakan oleh Aplikasi, termasuk preferensi pengguna (seperti mode gelap) dan data glosarium, disimpan secara lokal di perangkat Anda menggunakan database SQLite. Data ini tidak dikirim ke server mana pun.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "3. Hak Akses Aplikasi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Aplikasi ini tidak memerlukan akses ke data sensitif di perangkat Anda, seperti kontak, galeri, atau mikrofon.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Dengan menggunakan Aplikasi MaqraDars, Anda menyetujui kebijakan privasi ini.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}