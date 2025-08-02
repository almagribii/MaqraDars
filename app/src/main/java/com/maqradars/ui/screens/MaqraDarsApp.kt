import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.maqradars.Screen
import com.maqradars.ui.screens.AboutScreen
import com.maqradars.ui.screens.GlosariumScreen
import com.maqradars.ui.screens.MaqamDetailScreen
import com.maqradars.ui.screens.PrivacyPolicyScreen
import com.maqradars.ui.screens.RecitationTypeSelectionScreen
import com.maqradars.ui.screens.SettingsScreen
import com.maqradars.ui.screens.TilawahScreen
import com.maqradars.ui.viewmodel.MaqamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaqraDarsApp(
    navController: NavHostController,
    viewModel: MaqamViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            // Tampilkan TopAppBar hanya di layar MaqamList
            if (currentRoute == Screen.MaqamList.route) {
                TopAppBar(
                    title = { Text(text = "MaqraDars", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.primary
                            )

                        }
                        IconButton(onClick = { /* Handle klik ikon kedua */ }) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Bagikan",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                val items = listOf(Screen.MaqamList, Screen.Glosarium, Screen.Settings)
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute?.startsWith(screen.route) == true,
                        onClick = {
                            if (currentRoute?.startsWith(screen.route) == false) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.MaqamList.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.MaqamList.route) {
                MaqamListScreen(
                    viewModel = viewModel,
                    onMaqamClick = { maqamId, maqamName ->
                        navController.navigate("recitation_type_selection/$maqamId/$maqamName")
                    },
                    contentPadding = innerPadding
                )
            }
            composable("recitation_type_selection/{maqamId}/{maqamName}") { backStackEntry ->
                val maqamId = backStackEntry.arguments?.getString("maqamId")?.toLongOrNull() ?: 0L
                val maqamName = backStackEntry.arguments?.getString("maqamName") ?: ""
                RecitationTypeSelectionScreen(
                    maqamId = maqamId,
                    maqamName = maqamName,
                    onMujawwadClick = { id ->
                        navController.navigate("maqam_detail/$id")
                    },
                    onTilawahClick = { surahName ->
                        navController.navigate("tilawah_screen/$surahName")
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable("maqam_detail/{maqamId}") { backStackEntry ->
                val maqamId = backStackEntry.arguments?.getString("maqamId")?.toLongOrNull() ?: 0L
                MaqamDetailScreen(
                    viewModel = viewModel,
                    maqamId = maqamId,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable("tilawah_screen/{surahName}") { backStackEntry ->
                val surahName = backStackEntry.arguments?.getString("surahName") ?: "Tilawah"
                TilawahScreen(surahName = surahName, onBackClick = { navController.popBackStack() })
            }
            composable(Screen.Glosarium.route) {
                GlosariumScreen(viewModel = viewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = viewModel, navController = navController) // <-- PERBAIKAN DI SINI
            }
            composable("about_screen") {
                AboutScreen(onBackClick = { navController.popBackStack() })
            }
            composable("privacy_policy_screen") {
                PrivacyPolicyScreen(onBackClick = { navController.popBackStack() })
            }
        }
    }
}