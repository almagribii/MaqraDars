import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maqradars.R
import com.maqradars.data.entity.Maqam

@Composable
fun MaqamCardItem(
    maqam: Maqam,
    onMaqamClick: (Long, String) -> Unit,
) {
    Card(
        modifier = Modifier
            .size(110.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onMaqamClick(maqam.id, maqam.name) }

    ) {
        Column(
            modifier = Modifier.fillMaxSize()
            .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val imageResId = when (maqam.name) {
                "Hijaz" -> R.drawable.ic_hijaz
                "Bayyati" -> R.drawable.ic_bayati
                "Shoba" -> R.drawable.ic_sika
                "Rast" -> R.drawable.ic_rast
                "Sika" -> R.drawable.ic_sika
                "Jiharkah" -> R.drawable.ic_jiharkah
                "Nahawand" -> R.drawable.ic_nahawand
                else -> R.drawable.ic_bayati
            }
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Ikon Maqam ${maqam.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(50))
            )
            Text(
                text = maqam.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 8.dp),
            )

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