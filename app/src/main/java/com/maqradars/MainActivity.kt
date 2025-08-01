// app/src/main/java/com/maqradars/MainActivity.kt

package com.maqradars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maqradars.data.MaqraDarsDatabase
import com.maqradars.data.entity.Maqam
import com.maqradars.data.repository.MaqamRepository
import com.maqradars.ui.screens.MaqamDetailScreen
import com.maqradars.ui.theme.MaqraDarsTheme
import com.maqradars.ui.viewmodel.MaqamViewModel
import com.maqradars.ui.viewmodel.MaqamViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {

    private val database by lazy { MaqraDarsDatabase.getDatabase(this) }
    private val repository by lazy { MaqamRepository(database.maqamDao()) }
    private val viewModel: MaqamViewModel by viewModels { MaqamViewModelFactory(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

enableEdgeToEdge()
        addInitialData()

        setContent {
            MaqraDarsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MaqraDarsNavHost(navController, viewModel)
                }
            }
        }
    }

    private fun addInitialData() {
        // Meluncurkan coroutine untuk menambahkan data jika database kosong
        lifecycleScope.launch(Dispatchers.IO) {
            val maqamDao = database.maqamDao()
            // Menggunakan .first() untuk menunggu hasil dari Flow
            if (maqamDao.getAllMaqamat().first().isEmpty()) {
                maqamDao.insertMaqam(
                    Maqam(name = "Bayati", description = "Maqam dasar yang tenang, sering digunakan dalam pembukaan bacaan.", audioPathPureMaqam = "bayati.mp3", isFavorite = false)
                )
                maqamDao.insertMaqam(
                    Maqam(name = "Hijaz", description = "Maqam yang sedih dan penuh perasaan, cocok untuk kisah-kisah duka.", audioPathPureMaqam = "hijaz.mp3", isFavorite = false)
                )
                maqamDao.insertMaqam(
                    Maqam(name = "Nahawand", description = "Maqam yang penuh melodi dan ekspresif, sering digunakan untuk ayat-ayat yang membangkitkan semangat.", audioPathPureMaqam = "nahawand.mp3", isFavorite = false)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaqraDarsNavHost(
    navController: NavHostController,
    viewModel: MaqamViewModel
) {
    NavHost(navController = navController, startDestination = "maqam_list") {
        composable("maqam_list") {
            MaqamListScreen(
                viewModel = viewModel,
                onMaqamClick = { maqamId ->
                    navController.navigate("maqam_detail/$maqamId")
                }
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaqamListScreen(
    viewModel: MaqamViewModel,
    onMaqamClick: (Long) -> Unit
) {
    // Perbaikan di sini: deklarasi tipe Maqam secara eksplisit
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
    onMaqamClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMaqamClick(maqam.id) }
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