// app/src/main/java/com/maqradars/ui/screens/SettingsScreen.kt

package com.maqradars.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maqradars.ui.viewmodel.MaqamViewModel
import androidx.navigation.NavController

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
                .padding(16.dp)
        ) {
            Text(text = "Pengaturan Tampilan", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            // Pengaturan Mode Gelap
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Mode Gelap")
                Switch(
                    checked = user?.isDarkMode ?: false,
                    onCheckedChange = { isChecked ->
                        viewModel.updateIsDarkMode(isChecked)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("about_screen") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tentang Aplikasi")
            }
        }
    }
}