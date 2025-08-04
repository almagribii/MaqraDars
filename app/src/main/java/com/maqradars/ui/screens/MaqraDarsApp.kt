import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.maqradars.Screen
import com.maqradars.ui.screens.AboutScreen
import com.maqradars.ui.screens.AskQoriScreen
import com.maqradars.ui.screens.DaftarSuratScreen
import com.maqradars.ui.screens.DetailSuratScreen
import com.maqradars.ui.screens.GlosariumScreen
import com.maqradars.ui.screens.MaqamDetailScreen
import com.maqradars.ui.screens.MaqamListAllScreen
import com.maqradars.ui.screens.PrivacyPolicyScreen
import com.maqradars.ui.screens.RecitationTypeSelectionScreen
import com.maqradars.ui.screens.SettingsScreen
import com.maqradars.ui.screens.TilawahScreen
import com.maqradars.ui.viewmodel.MaqamViewModel

private const val API_KEY = "AIzaSyDW1VZe5D6vf9hyzhXm5B8PRj4pEk0YwsY"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaqraDarsApp(
    navController: NavHostController,
    viewModel: MaqamViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    Scaffold(
        topBar = {
            if (currentRoute == Screen.MaqamList.route) {
                TopAppBar(
                    title = { Text(text = "MaqraDars", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    actions = {
                        IconButton(onClick = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Ayo Pelajari Maqam Tilawah")
                                type = "text/plain"
                            }

                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Bagikan Aplikasi",
                                tint = MaterialTheme.colorScheme.primary
                            )

                        }
                        IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Pengaturan",
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
                    onAskQoriClick = {
                        navController.navigate(Screen.AskQori.route)
                    },

                    onListAll = {
                        navController.navigate("list_all")
                    },
                    onQuranClick = { // <--- Tambahkan fungsi ini
                        navController.navigate("quran_screen")
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
                GlosariumScreen(viewModel = viewModel, navController = navController)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = viewModel, navController = navController) // <-- PERBAIKAN DI SINI
            }

            composable(Screen.AskQori.route) {
                // Inisialisasi generativeModel dan chatInstance di dalam composable ini
                val generativeModel = remember {
                    GenerativeModel(
                        modelName = "gemini-1.5-flash",
                        apiKey = API_KEY,
                        systemInstruction = content {
                            text("Berperanlah sebagai seorang qori yang ramah, informatif, dan profesional. Berikan saran dan motivasi dalam mempelajari Al-Quran dan maknanya, jangan memberikan diagnosis atau resep spesifik, dan selalu sarankan untuk berkonsultasi langsung dengan guru jika ada keluhan serius. Setiap respons harus ringkas dan mudah dipahami. jangan bilang kamu adalah program komputer")
                        }
                    )
                }
                val chatInstance = remember { generativeModel.startChat() }

                // Sekarang `chatInstance` sudah ada dan bisa diteruskan
                AskQoriScreen(
                    onBackClick = { navController.popBackStack() },
                    chatInstance = chatInstance
                )
            }
            composable("about_screen") {
                AboutScreen(onBackClick = { navController.popBackStack() })
            }

            composable("list_all") {
                MaqamListAllScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onMaqamClick = { maqamId, maqamName ->
                        navController.navigate("recitation_type_selection/$maqamId/$maqamName")
                    }
                )
            }

            composable("privacy_policy_screen") {
                PrivacyPolicyScreen(onBackClick = { navController.popBackStack() })
            }
            composable("quran_screen") {
                DaftarSuratScreen(
                    onSuratClick = { nomorSurat ->
                        navController.navigate("detail_surat/$nomorSurat")
                    },
                    onBackClick = { navController.popBackStack() } // Tambahkan ini

                )
            }

            composable("detail_surat/{nomorSurat}") { backStackEntry ->
                val nomorSurat = backStackEntry.arguments?.getString("nomorSurat")?.toIntOrNull()
                if (nomorSurat != null) {
                    DetailSuratScreen(
                        nomorSurat = nomorSurat,
                        onBackClick = { navController.popBackStack() } // Tambahkan ini
                    )
                } else {
                }
            }

        }
    }
}