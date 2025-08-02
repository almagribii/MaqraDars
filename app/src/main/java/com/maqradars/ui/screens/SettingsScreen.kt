// app/src/main/java/com/maqradars/ui/screens/SettingsScreen.kt

package com.maqradars.ui.screens

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maqradars.ui.viewmodel.MaqamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: MaqamViewModel, navController: NavController) {
    val user by viewModel.user.collectAsState(initial = null)

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Pengaturan") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bagian Pengaturan Tampilan
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                SettingsItem(
                    icon = Icons.Default.Palette,
                    title = "Mode Gelap",
                    trailing = {
                        Switch(
                            checked = user?.isDarkMode ?: false,
                            onCheckedChange = { isChecked ->
                                viewModel.updateIsDarkMode(isChecked)
                            }
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bagian Tentang Aplikasi
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "Tentang Aplikasi",
                    onClick = { navController.navigate("about_screen") }
                )
                SettingsItem(
                    icon = Icons.AutoMirrored.Filled.List,
                    title = "Kebijakan Privasi",
                    onClick = { navController.navigate("privacy_policy_screen") }
                )
                SettingsItem(
                    icon = Icons.Default.Share,
                    title = "Beri Kami Semangat",
                    onClick = { /* TODO: Aksi untuk share atau rating */ }
                )

                val context = LocalContext.current
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    SettingsItem(
                        icon = Icons.AutoMirrored.Filled.ExitToApp, // Menggunakan ikon yang sesuai
                        title = "Keluar",
                        onClick = { (context as? Activity)?.finish() } // Fungsionalitas keluar
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        }
        if (trailing != null) {
            trailing()
        } else if (onClick != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}