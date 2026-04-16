import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.maqradars.R
import com.maqradars.data.entity.Maqam
import com.maqradars.ui.utils.ResponsiveUtils
import com.maqradars.ui.viewmodel.MaqamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaqamListScreen(
    viewModel: MaqamViewModel,
    onMaqamClick: (Long, String) -> Unit,
    onAskQoriClick: () -> Unit,
    onQuranClick: () -> Unit,
    onListAll: () -> Unit,
    contentPadding: PaddingValues
) {
    val maqamat by viewModel.allMaqamat.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    val contentPaddingValue = ResponsiveUtils.getContentPadding()
    
    val filteredMaqamat = maqamat.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(contentPaddingValue)
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
                        .padding(horizontal = contentPaddingValue),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    shape = RoundedCornerShape(30.dp)
                )
            }

            item {
                MaqamListBanner()
            }

            item {
                Button(
                    onClick = onQuranClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = contentPaddingValue)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Al-Quran", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            item {
                MaqamListHeader(onListAll = onListAll, contentPadding = contentPaddingValue)
            }

            item {
                MaqamListContent(filteredMaqamat = filteredMaqamat, onMaqamClick = onMaqamClick, contentPadding = contentPaddingValue)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        FloatingActionButton(
            onClick = onAskQoriClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .size(ResponsiveUtils.getFabSize()),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.AutoMirrored.Filled.Message, contentDescription = "Tanya Qori")
        }
    }
}

@Composable
private fun MaqamListBanner() {
    val bannerHeight = ResponsiveUtils.getBannerHeight()
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(bannerHeight)
            .padding(horizontal = ResponsiveUtils.getContentPadding(), vertical = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(16.dp)
            )
            .graphicsLayer { clip = false }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ustadzah),
            contentDescription = "Banner Image",
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
                .zIndex(1f)
                .background(color = MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = painterResource(R.drawable.bener_trans),
                contentDescription = "Banner Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = ResponsiveUtils.getContentPadding()),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 12.dp),
                    text = "Ayo Belajar Irama!",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Aplikasi Pembelajaran Maqam \n Quran dengan sistem Audio",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 14.sp,
                )
                Image(
                    painterResource(R.drawable.bintang5),
                    contentDescription = "Rating Stars",
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }
}

@Composable
private fun MaqamListHeader(onListAll: () -> Unit, contentPadding: Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = contentPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Daftar Maqam",

            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = onListAll) {
            Text(text = "Lihat Semua", fontSize = 14.sp)
        }
    }
}

@Composable
private fun MaqamListContent(
    filteredMaqamat: List<Maqam>,
    onMaqamClick: (Long, String) -> Unit,
    contentPadding: Dp
) {
    val gridColumns = ResponsiveUtils.getGridColumns()
    
    filteredMaqamat.chunked(gridColumns).forEach { rowItems ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = contentPadding),
            horizontalArrangement = if (rowItems.size < gridColumns) {
                Arrangement.Center
            } else {
                Arrangement.spacedBy(contentPadding)
            }
        ) {
            for (maqam in rowItems) {
                Box(modifier = Modifier.weight(1f)) {
                    MaqamCardItem(
                        maqam = maqam,
                        onMaqamClick = onMaqamClick
                    )
                }
            }
            if (rowItems.size < gridColumns) {
                repeat(gridColumns - rowItems.size) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
        Spacer(modifier = Modifier.height(contentPadding))
    }
}