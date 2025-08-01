// app/src/main/java/com/maqradars/MainActivity.kt

package com.maqradars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maqradars.data.MaqraDarsDatabase
import com.maqradars.data.entity.AyatExample
import com.maqradars.data.entity.GlosariumTerm
import com.maqradars.data.entity.Maqam
import com.maqradars.data.entity.MaqamVariant
import com.maqradars.data.repository.MaqamRepository
import com.maqradars.ui.screens.GlosariumScreen
import com.maqradars.ui.screens.MaqamDetailScreen
import com.maqradars.ui.screens.RecitationTypeSelectionScreen
import com.maqradars.ui.screens.SettingsScreen
import com.maqradars.ui.screens.TilawahScreen
import com.maqradars.ui.theme.MaqraDarsTheme
import com.maqradars.ui.viewmodel.MaqamViewModel
import com.maqradars.ui.viewmodel.MaqamViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.foundation.isSystemInDarkTheme
import android.content.res.Configuration
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import android.media.MediaPlayer
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import com.maqradars.data.entity.User
import com.maqradars.ui.screens.AboutScreen

class MainActivity : ComponentActivity() {

    private val database by lazy { MaqraDarsDatabase.getDatabase(this) }
    private val repository by lazy { MaqamRepository(database.maqamDao(), database.maqamVariantDao(), database.ayatExampleDao(), database.glosariumTermDao(), database.userDao()) }
    private val viewModel: MaqamViewModel by viewModels { MaqamViewModelFactory(repository) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isSystemInDarkMode = (applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        addInitialData(isSystemInDarkMode)

        setContent {
            val user by viewModel.user.collectAsState(initial = null)
            val isDarkMode = user?.isDarkMode ?: isSystemInDarkTheme()

            MaqraDarsTheme(darkTheme = isDarkMode) {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MaqraDarsApp(navController, viewModel)
                }
            }
        }
    }

    private fun addInitialData(isSystemInDarkMode: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val maqamDao = database.maqamDao()
            val maqamVariantDao = database.maqamVariantDao()
            val ayatExampleDao = database.ayatExampleDao()
            val glosariumTermDao = database.glosariumTermDao()
            val userDao = database.userDao()

            if (maqamDao.getAllMaqamat().first().isEmpty()) {
                val bayatiId = maqamDao.insertMaqam(
                    Maqam(name = "Bayati", description = "Maqam dasar yang tenang.", audioPathPureMaqam = "bayati.mp3", isFavorite = false)
                )
                val hijazId = maqamDao.insertMaqam(
                    Maqam(name = "Hijaz", description = "Maqam yang sedih dan penuh perasaan.", audioPathPureMaqam = "hijaz.mp3", isFavorite = false)
                )

                val bayatiNaqaId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(maqamId = bayatiId, variantName = "Naqa", description = "Varian Bayati Naqa", audioPath = "bayati_naqa.mp3")
                )
                val bayatiKurdId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(maqamId = bayatiId, variantName = "Kurd", description = "Varian Bayati Kurd", audioPath = "bayati_kurd.mp3")
                )
                ayatExampleDao.insertAyatExample(
                    AyatExample(maqamVariantId = bayatiNaqaId, surahNumber = 1, ayatNumber = 1, arabicText = "بِسْمِ ٱللّٰهِ ٱلرَّحْمٰنِ ٱلرَّحِيمِ", translationText = "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.", audioPath = "alfatihah_1")
                )
                ayatExampleDao.insertAyatExample(
                    AyatExample(maqamVariantId = bayatiNaqaId, surahNumber = 1, ayatNumber = 2, arabicText = "اَلْحَمْدُ لِلّٰهِ رَبِّ الْعٰلَمِيْنَ", translationText = "Segala puji bagi Allah, Tuhan seluruh alam,", audioPath = "alfatihah_2")
                )
                glosariumTermDao.insertGlosariumTerm(
                    GlosariumTerm(term = "Maqam", definition = "Tangga nada atau irama dalam pembacaan Al-Quran.")
                )
                glosariumTermDao.insertGlosariumTerm(
                    GlosariumTerm(term = "Tajwid", definition = "Ilmu yang mempelajari cara membaca huruf-huruf Al-Quran dengan benar.")
                )
            }
            if (userDao.getSingleUser().first() == null) {
                userDao.insertUser(User(username = "default_user", isDarkMode = isSystemInDarkMode))
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    data object MaqamList : Screen("maqam_list", "Maqamat", Icons.Default.Home)
    data object Glosarium : Screen("glosarium", "Glosarium", Icons.Default.Info)
    data object Settings : Screen("settings", "Pengaturan", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaqraDarsApp(
    navController: NavHostController,
    viewModel: MaqamViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
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
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.MaqamList.route) {
                MaqamListScreen(
                    viewModel = viewModel,
                    onMaqamClick = { maqamId, maqamName ->
                        navController.navigate("recitation_type_selection/$maqamId/$maqamName")
                    }
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaqamListScreen(
    viewModel: MaqamViewModel,
    onMaqamClick: (Long, String) -> Unit
) {
    val maqamat by viewModel.allMaqamat.collectAsState(initial = emptyList<Maqam>())
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Daftar Maqamat") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {

        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(maqamat) { maqam ->
                MaqamItem(maqam = maqam, onMaqamClick = onMaqamClick)
            }
        }
    }
}

@Composable
fun MaqamItem(
    maqam: Maqam,
    onMaqamClick: (Long, String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMaqamClick(maqam.id, maqam.name) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = maqam.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = maqam.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}