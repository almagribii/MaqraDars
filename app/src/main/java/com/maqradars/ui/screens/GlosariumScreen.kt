// app/src/main/java/com/maqradars/ui/screens/GlosariumScreen.kt

package com.maqradars.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maqradars.data.entity.GlosariumTerm
import com.maqradars.ui.theme.MaqraDarsTheme
import com.maqradars.ui.viewmodel.MaqamViewModel
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlosariumScreen(viewModel: MaqamViewModel) {
    var searchQuery by remember { mutableStateOf("") }

    val glosariumTerms by viewModel.allGlosariumTerms.collectAsState(initial = emptyList())
    val filteredTerms = glosariumTerms.filter {
        it.term.contains(searchQuery, ignoreCase = true) || it.definition.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Glosarium", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Hapus pencarian"
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
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Cari istilah...") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredTerms) { term ->
                    GlosariumItem(term = term)
                    Divider()
                }
            }
        }
    }
}

@Composable
fun GlosariumItem(term: GlosariumTerm) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = term.term,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = term.definition,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            contentPadding = PaddingValues(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            items(dummyTerms) { term ->
//                GlosariumItem(term = term)
//                Divider()
//            }
//        }
//    }
//}