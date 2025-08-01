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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maqradars.data.MaqraDarsDatabase
import com.maqradars.data.entity.Maqam
import com.maqradars.data.entity.MaqamVariant
import com.maqradars.data.repository.MaqamRepository
import com.maqradars.ui.screens.MaqamDetailScreen
import com.maqradars.ui.screens.RecitationTypeSelectionScreen
import com.maqradars.ui.screens.TilawahScreen
import com.maqradars.ui.theme.MaqraDarsTheme
import com.maqradars.ui.viewmodel.MaqamViewModel
import com.maqradars.ui.viewmodel.MaqamViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import android.media.MediaPlayer
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.filled.ArrowBack
import androidx.lifecycle.lifecycleScope

class MainActivity : ComponentActivity() {

    private val database by lazy { MaqraDarsDatabase.getDatabase(this) }
    private val repository by lazy { MaqamRepository(database.maqamDao(), database.maqamVariantDao()) }
    private val viewModel: MaqamViewModel by viewModels { MaqamViewModelFactory(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addInitialData()

        setContent {
            MaqraDarsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MaqraDarsApp(navController, viewModel)
                }
            }
        }
    }

    private fun addInitialData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val maqamDao = database.maqamDao()
            val maqamVariantDao = database.maqamVariantDao()
            if (maqamDao.getAllMaqamat().first().isEmpty()) {
                val bayatiId = maqamDao.insertMaqam(
                    Maqam(name = "Bayati", description = "Maqam dasar yang tenang.", audioPathPureMaqam = "bayati.mp3", isFavorite = false)
                )
                val hijazId = maqamDao.insertMaqam(
                    Maqam(name = "Hijaz", description = "Maqam yang sedih dan penuh perasaan.", audioPathPureMaqam = "hijaz.mp3", isFavorite = false)
                )

                maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(maqamId = bayatiId, variantName = "Naqa", description = "Varian Bayati Naqa", audioPath = "bayati_naqa.mp3")
                )
                maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(maqamId = bayatiId, variantName = "Kurd", description = "Varian Bayati Kurd", audioPath = "bayati_kurd.mp3")
                )
                maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(maqamId = hijazId, variantName = "Hijaz Kurd", description = "Varian Hijaz Kurd", audioPath = "hijaz_kurd.mp3")
                )
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    data object MaqamList : Screen("maqam_list", "Maqamat", Icons.Default.Home)
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
                val items = listOf(Screen.MaqamList, Screen.Settings)
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
                    onTilawahClick = {
                        navController.navigate("tilawah_screen")
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
            composable("tilawah_screen") {
                TilawahScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
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
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.insertMaqam(Maqam(name = "Rast", description = "Maqam klasik yang serius dan formal.", audioPathPureMaqam = "rast.mp3", isFavorite = false))
                    }
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah Maqam")
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecitationTypeSelectionScreen(
    maqamId: Long,
    maqamName: String,
    onMujawwadClick: (Long) -> Unit,
    onTilawahClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Pilih Bacaan: $maqamName") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Pilih Jenis Bacaan", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { onMujawwadClick(maqamId) },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(text = "Mujawwad", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onTilawahClick,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(text = "Tilawah", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaqamDetailScreen(
    viewModel: MaqamViewModel,
    maqamId: Long,
    onBackClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var maqam by remember { mutableStateOf<Maqam?>(null) }

    // State untuk mengontrol pemutar audio
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val context = LocalContext.current

    LaunchedEffect(maqamId) {
        coroutineScope.launch {
            maqam = viewModel.getMaqamById(maqamId)
        }
    }

    DisposableEffect(maqam) {
        if (maqam != null) {
            val audioFileName = maqam!!.audioPathPureMaqam.removeSuffix(".mp3")
            val audioResourceId = context.resources.getIdentifier(audioFileName, "raw", context.packageName)
            if (audioResourceId != 0) {
                mediaPlayer = MediaPlayer.create(context, audioResourceId)
            }
        }

        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = maqam?.name ?: "Detail Maqam") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (maqam != null) {
                Text(
                    text = maqam!!.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { mediaPlayer?.start() }) {
                        Text("Putar")
                    }
                    Button(onClick = { mediaPlayer?.pause() }) {
                        Text("Jeda")
                    }
                    Button(onClick = {
                        mediaPlayer?.apply {
                            stop()
                            prepareAsync()
                        }
                    }) {
                        Text("Hentikan")
                    }
                }

            } else {
                Text(
                    text = "Maqam tidak ditemukan.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TilawahScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tilawah Al-Fatihah") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Memutar Surah Al-Fatihah...", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))
            // TODO: Tambahkan kontrol audio di sini
        }
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Ini adalah Halaman Pengaturan", style = MaterialTheme.typography.headlineSmall)
        Text(text = "Fungsionalitas akan ditambahkan di sini.", style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MaqamListPreview() {
    MaqraDarsTheme {
        val dummyMaqamat = listOf(
            Maqam(name = "Bayati", description = "Maqam dasar yang tenang", audioPathPureMaqam = "", isFavorite = false),
            Maqam(name = "Hijaz", description = "Maqam yang sedih dan penuh perasaan", audioPathPureMaqam = "", isFavorite = false),
        )
        Scaffold(
            topBar = { TopAppBar(title = { Text("Daftar Maqamat") }) }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dummyMaqamat) { maqam ->
                    MaqamItem(maqam = maqam, onMaqamClick = { _, _ -> })
                }
            }
        }
    }
}