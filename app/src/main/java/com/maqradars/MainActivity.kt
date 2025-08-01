// app/src/main/java/com/maqradars/MainActivity.kt

package com.maqradars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.maqradars.data.MaqraDarsDatabase
import com.maqradars.data.entity.Maqam
import com.maqradars.data.repository.MaqamRepository
import com.maqradars.ui.theme.MaqraDarsTheme
import com.maqradars.ui.viewmodel.MaqamViewModel
import com.maqradars.ui.viewmodel.MaqamViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val database by lazy { MaqraDarsDatabase.getDatabase(this) }
    private val repository by lazy { MaqamRepository(database.maqamDao()) }
    private val viewModel: MaqamViewModel by viewModels { MaqamViewModelFactory(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Menambahkan data awal ke database saat aplikasi pertama kali dibuat
        // Ini hanya untuk contoh. Anda bisa menghapus ini setelah database terisi.
        addInitialData()

        setContent {
            MaqraDarsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MaqamListScreen(viewModel)
                }
            }
        }
    }

    private fun addInitialData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val maqamDao = database.maqamDao()
            if (maqamDao.getAllMaqamat().first().isEmpty()) {
                maqamDao.insertMaqam(
                    Maqam(
                        name = "Bayati",
                        description = "Maqam dasar yang tenang",
                        audioPathPureMaqam = "bayati.mp3",
                        isFavorite = false
                    )
                )
                maqamDao.insertMaqam(
                    Maqam(
                        name = "Hijaz",
                        description = "Maqam yang sedih dan penuh perasaan",
                        audioPathPureMaqam = "hijaz.mp3",
                        isFavorite = false
                    )
                )
                maqamDao.insertMaqam(
                    Maqam(
                        name = "Nahawand",
                        description = "Maqam yang penuh melodi dan ekspresif",
                        audioPathPureMaqam = "nahawand.mp3",
                        isFavorite = false
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaqamListScreen(viewModel: MaqamViewModel) {
    val maqamat by viewModel.allMaqamat.collectAsState(initial = emptyList())

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
            FloatingActionButton(onClick = { /* TODO: Tambah Maqam */ }) {
                Text(text = "+")
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
                MaqamItem(maqam = maqam)
            }
        }
    }
}
@Composable
fun MaqamItem(maqam: Maqam) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = maqam.name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = maqam.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MaqamListPreview() {
    MaqraDarsTheme {
        val dummyMaqamat = listOf(
            Maqam(
                name = "Bayati",
                description = "Maqam dasar yang tenang",
                audioPathPureMaqam = "",
                isFavorite = false
            ),
            Maqam(
                name = "Hijaz",
                description = "Maqam yang sedih dan penuh perasaan",
                audioPathPureMaqam = "",
                isFavorite = false
            ),
        )
        Scaffold(
            topBar = { TopAppBar(title = { Text("Daftar Maqamat") }) }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dummyMaqamat) { maqam ->
                    MaqamItem(maqam = maqam)
                }
            }
        }
    }
}