package com.example.strmtv.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.strmtv.data.model.Item

@Composable
fun HomeScreen(viewModel: MainViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    when (state) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Error -> ErrorScreen((state as UiState.Error).message)
        is UiState.Success -> ItemList((state as UiState.Success).items)
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun ItemList(items: List<Item>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(items) { item ->
            ItemCard(item)
        }
    }
}

@Composable
fun ItemCard(item: Item) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(item.poster?.trim()),
                contentDescription = null,
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${item.year} • ${item.genre}",
                    style = MaterialTheme.typography.bodySmall
                )
                if (item.type == "series") {
                    Text(
                        text = "Temporadas: ${item.seasons ?: "N/A"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}