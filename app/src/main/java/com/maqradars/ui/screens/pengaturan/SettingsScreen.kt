package com.maqradars.ui.screens.pengaturan

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maqradars.data.entity.User
import com.maqradars.ui.viewmodel.MaqamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: MaqamViewModel, navController: NavController) {
    val user by viewModel.user.collectAsState(initial = null)
    val activity = LocalContext.current as? Activity

    Scaffold(
        topBar = {
            SettingsAppBar(navController, activity)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsDisplaySection(user, viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            SettingsInfoSection(navController, activity)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsAppBar(navController: NavController, activity: Activity?) {
    TopAppBar(
        title = { Text(text = "Pengaturan", fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(onClick = { navController.navigate("about_screen") }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Tentang Aplikasi",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { activity?.finish() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Keluar Aplikasi",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
private fun SettingsDisplaySection(
    user: Any?,
    viewModel: MaqamViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        SettingsItem(
            icon = Icons.Default.Palette,
            title = "Mode Gelap",
            trailing = {
                Switch(
                    checked = (user as? User)?.isDarkMode ?: false,
                    onCheckedChange = { isChecked ->
                        viewModel.updateIsDarkMode(isChecked)
                    }
                )
            }
        )
    }
}

@Composable
private fun SettingsInfoSection(navController: NavController, activity: Activity?) {
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
            onClick = { navController.navigate("support_screen") }
        )
        SettingsItem(
            icon = Icons.Default.Call,
            title = "Hubungi Kami",
            onClick = { navController.navigate("contact_screen") }
        )
        SettingsItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = "Keluar",
            onClick = { activity?.finish() }
        )
    }
}

@Composable
private fun SettingsItem(
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
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal
            )
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