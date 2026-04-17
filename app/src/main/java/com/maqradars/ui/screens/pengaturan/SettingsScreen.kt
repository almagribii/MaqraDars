package com.maqradars.ui.screens.pengaturan

import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maqradars.notification.NotificationScheduler

@Composable
fun SettingsScreen(navController: NavController) {
    val activity = LocalContext.current as? Activity
    val context = LocalContext.current
    var isNotificationEnabled by remember { mutableStateOf(true) }
    var notificationHour by remember { mutableStateOf(17) }
    var notificationMinute by remember { mutableStateOf(10) }
    var showTimePickerDialog by remember { mutableStateOf(false) }

    // Permission launcher untuk POST_NOTIFICATIONS (Android 13+)
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && isNotificationEnabled) {
            NotificationScheduler.scheduleNotifications(context)
        }
    }

    // Load preference saat screen terbuka
    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("maqradars_prefs", android.content.Context.MODE_PRIVATE)
        isNotificationEnabled = sharedPref.getBoolean("notifications_enabled", true)
        
        val (hour, minute) = NotificationScheduler.getNotificationTime(context)
        notificationHour = hour
        notificationMinute = minute
    }

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
            SettingsDisplaySection(
                isNotificationEnabled = isNotificationEnabled,
                notificationHour = notificationHour,
                notificationMinute = notificationMinute,
                onNotificationChange = { newState ->
                    isNotificationEnabled = newState
                    
                    // Simpan preference
                    val sharedPref = context.getSharedPreferences("maqradars_prefs", android.content.Context.MODE_PRIVATE)
                    sharedPref.edit().putBoolean("notifications_enabled", newState).apply()
                    
                    // Schedule atau cancel notifikasi
                    if (newState) {
                        // Request permission jika Android 13+
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            NotificationScheduler.scheduleNotifications(context)
                        }
                    } else {
                        NotificationScheduler.cancelNotifications(context)
                    }
                },
                onTimeChange = { showTimePickerDialog = true }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsInfoSection(navController, activity)
        }
        
        // Time Picker Dialog
        if (showTimePickerDialog) {
            TimePickerDialog(
                initialHour = notificationHour,
                initialMinute = notificationMinute,
                onConfirm = { hour, minute ->
                    notificationHour = hour
                    notificationMinute = minute
                    NotificationScheduler.setNotificationTime(context, hour, minute)
                    showTimePickerDialog = false
                },
                onDismiss = { showTimePickerDialog = false }
            )
        }
    }
}

@Composable
private fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var hour by remember { mutableStateOf(initialHour) }
    var minute by remember { mutableStateOf(initialMinute) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Atur Waktu Notifikasi", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hour Picker
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(onClick = { hour = if (hour == 23) 0 else hour + 1 }) {
                            Text("▲", fontSize = 20.sp)
                        }
                        Text(
                            text = String.format("%02d", hour),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        IconButton(onClick = { hour = if (hour == 0) 23 else hour - 1 }) {
                            Text("▼", fontSize = 20.sp)
                        }
                    }

                    Text(":", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(horizontal = 8.dp))

                    // Minute Picker
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(onClick = { minute = if (minute == 59) 0 else minute + 1 }) {
                            Text("▲", fontSize = 20.sp)
                        }
                        Text(
                            text = String.format("%02d", minute),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        IconButton(onClick = { minute = if (minute == 0) 59 else minute - 1 }) {
                            Text("▼", fontSize = 20.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(hour, minute) }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
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
    isNotificationEnabled: Boolean,
    notificationHour: Int,
    notificationMinute: Int,
    onNotificationChange: (Boolean) -> Unit,
    onTimeChange: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        SettingsItem(
            icon = Icons.Default.Notifications,
            title = "Notifikasi",
            trailing = {
                Switch(
                    checked = isNotificationEnabled,
                    onCheckedChange = onNotificationChange
                )
            }
        )
        
        if (isNotificationEnabled) {
            Divider(modifier = Modifier.fillMaxWidth())
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Waktu Notifikasi",
                onClick = onTimeChange,
                trailing = {
                    Text(
                        text = String.format("%02d:%02d", notificationHour, notificationMinute),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }
            )
        }
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