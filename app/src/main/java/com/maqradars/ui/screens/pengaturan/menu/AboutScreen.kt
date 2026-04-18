package com.maqradars.ui.screens.pengaturan.menu

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.maqradars.R
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tentang Aplikasi",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // ===== APP BRANDING (Minimal) =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_maqradars_trans),
                        contentDescription = "MaqraDars",
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Fit
                    )


                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "MaqraDars",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Pelajari Maqam Al-Qur'an dengan Mudah",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                SectionTitle(title = "Tentang Aplikasi")

                Spacer(modifier = Modifier.height(12.dp))

                AppDescriptionCard(
                    description = "Maqradars adalah aplikasi pembelajaran komprehensif untuk memahami dan menguasai berbagai Maqam (gaya nyanyian) dalam membaca Al-Qur'an. Dengan antarmuka yang intuitif dan konten berkualitas tinggi, kami membantu Anda menjadi pembaca Al-Qur'an yang lebih baik.",
                    icon = Icons.Default.Info
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ===== FITUR UTAMA =====
                SectionTitle(title = "Fitur Utama")

                Spacer(modifier = Modifier.height(12.dp))

                FeatureItem(
                    icon = Icons.Default.AudioFile,
                    title = "Audio Berkualitas Tinggi",
                    description = "Dengarkan contoh maqam dari pembaca profesional"
                )

                Spacer(modifier = Modifier.height(10.dp))

                FeatureItem(
                    icon = Icons.Default.School,
                    title = "Pembelajaran Terstruktur",
                    description = "Materi disusun dari dasar hingga mahir"
                )

                Spacer(modifier = Modifier.height(10.dp))

                FeatureItem(
                    icon = Icons.Default.OfflinePin,
                    title = "Mode Offline",
                    description = "Belajar kapan saja tanpa koneksi internet"
                )

                Spacer(modifier = Modifier.height(10.dp))

                FeatureItem(
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    title = "Glosarium Lengkap",
                    description = "Referensi istilah dan penjelasan Qur'anik"
                )

                Spacer(modifier = Modifier.height(28.dp))

                SectionTitle(title = "Tim Pengembang")

                Spacer(modifier = Modifier.height(12.dp))

                TeamCard(
                    photoDrawableId = R.drawable.dev_profile,
                    name = "Brucad Al Magribi",
                    role = "Lead Developer",
                    description = "Pengembang utama aplikasi dan arsitektur sistem",
                    cardColor = MaterialTheme.colorScheme.primaryContainer,
                    onContactClick = {
                        val whatsappUrl = "https://wa.me/6282210980898?text=Assalamu%20alaikum,%20saya%20ingin%20menghubungi%20Brucad%20Al%20Magribi"
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = whatsappUrl.toUri()
                        }
                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            // Fallback jika WhatsApp tidak terinstall
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                TeamCard(
                    photoDrawableId = R.drawable.dev_profile,
                    name = "Muhammad Ghatan Alif",
                    role = "Voice Actor",
                    description = "Profesional dalam pembacaan Al-Qur'an",
                    cardColor = MaterialTheme.colorScheme.tertiaryContainer,
                    onContactClick = {
                        val whatsappUrl = "https://wa.me/6282254849341?text=Assalamu%20alaikum,%20saya%20ingin%20menghubungi%20Muhammad%20Ghatan%20Alif"
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = whatsappUrl.toUri()
                        }
                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                TeamCard(
                    photoDrawableId = R.drawable.dev_profile,
                    name = "Faizal Zein",
                    role = "Voice Actor",
                    description = "Ahli dalam berbagai gaya maqam",
                    cardColor = MaterialTheme.colorScheme.secondaryContainer,
                    onContactClick = {
                        val whatsappUrl = "https://wa.me/6285741750158?text=Assalamu%20alaikum,%20saya%20ingin%20menghubungi%20Faizal%20Zein"
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = whatsappUrl.toUri()
                        }
                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {
                        }
                    }
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ===== FOOTER =====
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Versi 1.0.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "© 2024 Maqradars. All rights reserved.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AppDescriptionCard(description: String, icon: ImageVector) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Description",
                modifier = Modifier
                    .size(28.dp)
                    .padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
fun FeatureItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TeamCard(
    photoDrawableId: Int,
    name: String,
    role: String,
    description: String,
    cardColor: Color,
    onContactClick: (() -> Unit)?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, cardColor.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Profile Picture
                Image(
                    painter = painterResource(id = photoDrawableId),
                    contentDescription = name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, cardColor, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = role,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E7D32),
                        fontSize = 12.sp
                    )
                }

                // Contact Button
                if (onContactClick != null) {
                    IconButton(
                        onClick = onContactClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "WhatsApp",
                            tint = cardColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}