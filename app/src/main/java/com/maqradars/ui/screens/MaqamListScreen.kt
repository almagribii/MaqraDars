import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.maqradars.R
import com.maqradars.ui.viewmodel.MaqamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaqamListScreen(
    viewModel: MaqamViewModel,
    onMaqamClick: (Long, String) -> Unit,
    // Parameter padding untuk menampung nilai dari Scaffold di luar
    contentPadding: PaddingValues
) {
    val maqamat by viewModel.allMaqamat.collectAsState(initial = emptyList())

    // Hapus Scaffold di sini
    Column(
        modifier = Modifier
            .fillMaxSize()
            // Gunakan padding yang diterima dari parameter
            .padding(contentPadding)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .graphicsLayer { clip = false }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFD2E8FF), shape = RoundedCornerShape(12.dp))
                        .zIndex(1f)
                ) {
                    Text(
                        text = "Ayo Hitung \n Pecahan !",
                        color = Color(0xFFFBA834),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 30.dp)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ustadzah),
                    contentDescription = "Kid Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(500.dp)
                        .align(Alignment.CenterStart)
                        .offset(x = (-80).dp)
                        .zIndex(5f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daftar Maqam",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { /* TODO: Navigasi ke semua daftar */ }) {
                    Text(text = "Lihat Semua")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 0.dp)
            ) {
                items(maqamat) { maqam ->
                    MaqamCardItem(maqam = maqam, onMaqamClick = onMaqamClick)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clickable {
                        // TODO: Navigasi ke Glosarium
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Akses Cepat: Glosarium",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
