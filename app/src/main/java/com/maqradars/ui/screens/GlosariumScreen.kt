// app/src/main/java/com/maqradars/ui/screens/GlosariumScreen.kt

package com.maqradars.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maqradars.data.entity.GlosariumTerm
import com.maqradars.ui.theme.MaqraDarsTheme
import com.maqradars.ui.viewmodel.MaqamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlosariumScreen(viewModel: MaqamViewModel) {
    val glosariumTerms by viewModel.allGlosariumTerms.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Glosarium") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(glosariumTerms) { term ->
                GlosariumItem(term = term)
            }
        }
    }
}

@Composable
fun GlosariumItem(term: GlosariumTerm) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = term.term, style = MaterialTheme.typography.titleLarge)
            Text(text = term.definition, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GlosariumScreenPreview() {
//    MaqraDarsTheme {
//        val dummyTerms = listOf(
//            GlosariumTerm(term = "Maqam", definition = "Tangga nada atau irama dalam pembacaan Al-Quran."),
//            GlosariumTerm(term = "Tajwid", definition = "Ilmu yang mempelajari cara membaca Al-Quran dengan benar.")
//        )
//        // Preview dengan data dummy
//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            contentPadding = PaddingValues(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(dummyTerms) { term ->
//                GlosariumItem(term = term)
//            }
//        }
//    }
//}