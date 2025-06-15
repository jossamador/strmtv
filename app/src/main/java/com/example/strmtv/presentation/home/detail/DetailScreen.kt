package com.example.strmtv.presentation.home.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.strmtv.data.model.Item
import com.example.strmtv.presentation.home.SharedViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.example.strmtv.presentation.home.RecommendationsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    item: Item,
    onBack: () -> Unit,
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(item.id) {
        sharedViewModel.selectItem(item)
    }

    val recommendationsUiState by sharedViewModel.recoState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = item.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = item.backdrop,
                contentDescription = "Backdrop",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = item.poster,
                    contentDescription = "Poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(130.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "${item.title} (${item.year})",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tipo: ${item.type}", style = MaterialTheme.typography.bodySmall)
                    Text("Media: ${item.mediaType}", style = MaterialTheme.typography.bodySmall)
                    Text("⭐ ${item.rating} (${item.voteCount} votos)", style = MaterialTheme.typography.bodySmall)
                    Text("Duración: ${item.duration ?: "No disponible"}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                if (item.genres.isNotEmpty()) {
                    Text("Géneros: ${item.genres.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text("Fecha de estreno: ${item.releaseDate ?: "No disponible"}", style = MaterialTheme.typography.bodySmall)
                Text("Idioma: ${item.language ?: "No disponible"}", style = MaterialTheme.typography.bodySmall)
                Text("País: ${item.country ?: "No disponible"}", style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(12.dp))

                item.director?.let {
                    Text("Director: $it", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (item.cast.isNotEmpty()) {
                    Text("Reparto:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(item.cast.joinToString(", "), style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (item.availableOn.isNotEmpty()) {
                    Text("Disponible en:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item.availableOn.forEach { platform ->
                            AssistChip(onClick = {}, label = { Text(platform) })
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item.trailerUrl?.takeIf { it.isNotBlank() }?.let { url ->
                    Text(
                        text = "Ver tráiler",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item.overview?.let {
                    Text("Descripción", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(it, style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (recommendationsUiState is RecommendationsUiState.Success) {
                    val recommendedItems = (recommendationsUiState as RecommendationsUiState.Success).data
                    Text("Recomendaciones similares", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                        items(recommendedItems) { recommended ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                AsyncImage(
                                    model = recommended.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
                                        ?: "https://via.placeholder.com/100x150", // o una imagen por defecto
                                    contentDescription = recommended.title.orEmpty(),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(width = 100.dp, height = 150.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { sharedViewModel.selectItem(item) }
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = recommended.title.orEmpty(),
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}