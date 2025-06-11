package com.example.strmtv.presentation.home.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.strmtv.data.model.Item
import com.example.strmtv.data.model.remote.TMDBItem
import com.example.strmtv.presentation.home.navigation.Routes
import android.net.Uri
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    var query by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val useLocalSource by viewModel.useLocalSource.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar en TMDB o Local") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (query.isNotBlank()) {
                        viewModel.search(query)
                    }
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text("Buscar")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = if (useLocalSource) "Local" else "TMDB")
            Switch(
                checked = useLocalSource,
                onCheckedChange = { viewModel.setUseLocalSource(it) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is SearchUiState.Idle -> {
                Text("Escribe un título para buscar", style = MaterialTheme.typography.bodyLarge)
            }

            is SearchUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            is SearchUiState.Error -> {
                Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
            }

            is SearchUiState.SuccessLocal -> {
                if (state.results.isEmpty()) {
                    Text("No se encontraron resultados")
                } else {
                    ResultsListLocal(state.results, navController)
                }
            }

            is SearchUiState.SuccessTMDB -> {
                if (state.results.isEmpty()) {
                    Text("No se encontraron resultados")
                } else {
                    ResultsListTMDB(state.results, navController)
                }
            }
        }
    }
}

@Composable
fun ResultsListLocal(results: List<Item>, navController: NavHostController) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(results) { result ->
            ResultItemLocal(result = result, navController = navController)
        }
    }
}

@Composable
fun ResultsListTMDB(results: List<TMDBItem>, navController: NavHostController) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(results) { result ->
            ResultItemTMDB(result = result, navController = navController)
        }
    }
}

@Composable
fun ResultItemLocal(result: Item, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                navController.navigate(
                    "${Routes.DETAIL}/${Uri.encode(result.title)}/${Uri.encode(result.poster)}/${Uri.encode(result.year.toString())}/${Uri.encode(result.genre)}"
                )
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(result.poster ?: ""),
                contentDescription = null,
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = result.title ?: "Sin título",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}


@Composable
fun ResultItemTMDB(result: TMDBItem, navController: NavHostController) {
    // Armamos el posterPath SIN el "/" inicial
    val posterPath = result.poster?.removePrefix("/") ?: "no_image"
    val posterUrl = if (posterPath == "no_image") "" else "https://image.tmdb.org/t/p/w500/$posterPath"

    // Codificamos los campos
    val encodedPoster = URLEncoder.encode(posterPath, StandardCharsets.UTF_8.toString())
    val encodedTitle = URLEncoder.encode(result.title ?: result.name ?: "Sin título", StandardCharsets.UTF_8.toString())
    val encodedReleaseDate = URLEncoder.encode(result.releaseDate?.takeIf { it.isNotBlank() } ?: "Desconocido", StandardCharsets.UTF_8.toString())
    val encodedOverview = URLEncoder.encode(result.overview?.takeIf { it.isNotBlank() } ?: "Sin descripción disponible.", StandardCharsets.UTF_8.toString())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                navController.navigate(
                    "${Routes.DETAIL}/$encodedTitle/$encodedPoster/$encodedReleaseDate/$encodedOverview"
                )
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(posterUrl),
                contentDescription = null,
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = result.title ?: result.name ?: "Sin título",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}