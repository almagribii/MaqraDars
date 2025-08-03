import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.ComponentActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.maqradars.R
import com.maqradars.data.entity.Maqam
import com.maqradars.ui.viewmodel.MaqamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaqamListScreen(
    viewModel: MaqamViewModel,
    onMaqamClick: (Long, String) -> Unit,
    onAskQoriClick:() -> Unit,
    onListAll:() -> Unit,
    contentPadding: PaddingValues,


) {
    val maqamat by viewModel.allMaqamat.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val filteredMaqamat = maqamat.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()

        ) {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Cari maqam...") },
                    singleLine = true,
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                        .height(56.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    shape = RoundedCornerShape(30.dp)
                )
            }

            // Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .padding(horizontal = 14.dp, vertical = 15.dp)
                        .graphicsLayer { clip = false }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ustadzah),
                        contentDescription = "Kid Image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(400.dp)
                            .align(Alignment.CenterEnd)
                            .offset(x = (110).dp)
                            .zIndex(5f)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
//                            .background(
//                                color = MaterialTheme.colorScheme.primary,
//                                shape = RoundedCornerShape(12.dp)
//                            )
                            .zIndex(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bener),
                            contentDescription = "Bener",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 15.dp),
                            horizontalAlignment = Alignment.Start // Agar semua teks rata kanan
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 12.dp),
                                text = "Ayo Belajar Irama!",
                                color = Color.Black,
                                fontSize = 24.sp, // Ukuran font untuk baris pertama
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "Aplikasi Pembelajaran Maqam Quran dengan sistem Audio",
                                color = Color.Black,
                                fontSize = 14.sp, // Ukuran font untuk baris ketiga
                                fontWeight = FontWeight.Bold, // Bisa diubah juga
                            )
                        }
                    }

                }
            }

            item {
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 14.dp)
                ) {
                    Icon(Icons.Default.Book, contentDescription = "Al-Quran")
                    Text("Al-Quran")
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daftar Maqam",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onListAll) {
                        Text(text = "Lihat Semua")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(filteredMaqamat.chunked(3)) { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    for (maqam in rowItems) {
                        Box(modifier = Modifier.weight(1f)) {
                            MaqamCardItem(
                                maqam = maqam,
                                onMaqamClick = onMaqamClick
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        FloatingActionButton(
            onClick = onAskQoriClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .size(60.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary

        ) {
            Icon(Icons.Filled.Message, contentDescription = "Tambah")
        }
    }
}