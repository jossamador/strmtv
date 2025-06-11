package com.example.strmtv.presentation.home.detail

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun DetailScreen(
    navController: NavHostController,
    poster: String?,
    title: String?,
    releaseDate: String?,
    description: String?
) {
    // Decodificamos los parámetros recibidos
    val decodedPoster = URLDecoder.decode(poster ?: "no_image", StandardCharsets.UTF_8.toString())
    val decodedTitle = URLDecoder.decode(title ?: "Sin título", StandardCharsets.UTF_8.toString())
    val decodedReleaseDate = URLDecoder.decode(releaseDate ?: "Desconocido", StandardCharsets.UTF_8.toString())
    val decodedDescription = URLDecoder.decode(description ?: "Sin descripción disponible.", StandardCharsets.UTF_8.toString())

    // Reconstruimos bien la URL del poster
    val posterUrl = if (decodedPoster == "no_image") {
        "" // Aquí si quieres puedes poner una imagen local de "sin imagen"
    } else {
        "https://image.tmdb.org/t/p/w500/$decodedPoster"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(posterUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = decodedTitle,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Fecha de estreno: $decodedReleaseDate",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Descripción:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = decodedDescription,
            style = MaterialTheme.typography.bodySmall
        )
    }
}