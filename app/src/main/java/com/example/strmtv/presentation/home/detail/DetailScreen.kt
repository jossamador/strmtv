package com.example.strmtv.presentation.home.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.strmtv.data.model.Item
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(item: Item, onBack: () -> Unit) {
    val context = LocalContext.current

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
            // Backdrop image
            AsyncImage(
                model = item.backdrop,
                contentDescription = "${item.title} backdrop",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Poster + Main info
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = item.poster,
                    contentDescription = "${item.title} poster",
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
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tipo: ${item.type}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Media: ${item.mediaType}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "⭐ ${item.rating} (${item.voteCount} votos)",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Duración: ${item.duration.ifBlank { "No disponible" }}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Genres & info
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                if (item.genres.isNotEmpty()) {
                    Text(
                        text = "Géneros: ${item.genres.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = "Fecha de estreno: ${item.releaseDate.ifBlank { "No disponible" }}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Idioma: ${item.language.ifBlank { "No disponible" }}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "País: ${item.country.ifBlank { "No disponible" }}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Director
                if (item.director.isNotBlank()) {
                    Text(
                        text = "Director: ${item.director}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Cast
                if (item.cast.isNotEmpty()) {
                    Text(
                        text = "Reparto:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.cast.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Available on (chips)
                if (item.availableOn.isNotEmpty()) {
                    Text(
                        text = "Disponible en:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item.availableOn.forEach { platform ->
                            AssistChip(
                                onClick = { /* No action for now */ },
                                label = { Text(platform) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Trailer URL
                if (item.trailerUrl.isNotBlank()) {
                    Text(
                        text = "Ver tráiler",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.trailerUrl))
                                context.startActivity(intent)
                            }
                            .padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Overview
                if (item.overview.isNotBlank()) {
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.overview,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}