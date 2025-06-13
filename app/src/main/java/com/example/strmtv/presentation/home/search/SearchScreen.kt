package com.example.strmtv.presentation.home.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.strmtv.data.model.Item
import com.example.strmtv.data.model.remote.TMDBItem
import com.example.strmtv.presentation.home.SharedViewModel
import com.example.strmtv.presentation.home.navigation.Routes
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.background

@Composable
fun SearchScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    var query by remember { mutableStateOf("") }
    val uiState by searchViewModel.uiState.collectAsState()
    val useLocalSource by searchViewModel.useLocalSource.collectAsState()

    val tabs = listOf("Top Results", "TV Shows", "Movies", "Cast & Crew")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        // Barra de búsqueda
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                searchViewModel.search(query)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Buscar en TMDB o Local") }
        )

        // Botón para cambiar fuente
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { searchViewModel.setUseLocalSource(true) },
                enabled = !useLocalSource
            ) {
                Text("Buscar en Local")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { searchViewModel.setUseLocalSource(false) },
                enabled = useLocalSource
            ) {
                Text("Buscar en TMDB")
            }
        }

        // Tabs
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        searchViewModel.setUseLocalSource(index == 0)
                    },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Resultados
        when (uiState) {
            is SearchUiState.Idle -> {}
            is SearchUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is SearchUiState.SuccessLocal -> {
                ResultsListLocal(
                    results = (uiState as SearchUiState.SuccessLocal).results,
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
            is SearchUiState.SuccessTMDB -> {
                ResultsListTMDB(
                    results = (uiState as SearchUiState.SuccessTMDB).results,
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
            is SearchUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${(uiState as SearchUiState.Error).message}")
                }
            }
        }
    }
}

@Composable
fun ResultsListLocal(
    results: List<Item>,
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(results) { result ->
            ResultItemLocal(result = result, navController = navController, sharedViewModel = sharedViewModel)
        }
    }
}

@Composable
fun ResultsListTMDB(
    results: List<TMDBItem>,
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(results) { result ->
            ResultItemTMDB(result = result, navController = navController, sharedViewModel = sharedViewModel)
        }
    }
}

@Composable
fun ResultItemLocal(
    result: Item,
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Tarjeta alta, como en Apple TV+
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(0.dp), // sin sombra, estilo limpio
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // imagen ocupa todo el card
        )
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(result.poster ?: ""),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            // Texto sobre la imagen (opcional)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(Color(0x66000000)) // fondo semi-transparente
                    .padding(8.dp)
            ) {
                Text(
                    text = result.title ?: "Sin título",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ResultItemTMDB(
    result: TMDBItem,
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val posterPath = result.posterPath?.removePrefix("/") ?: ""
    val posterUrl = "https://image.tmdb.org/t/p/w500/$posterPath"

    val item = Item(
        id = 0,
        title = result.title ?: result.name ?: "",
        type = result.mediaType ?: "",
        year = 0,
        genre = "",
        genres = emptyList(),
        poster = posterUrl,
        backdrop = "",
        overview = result.overview ?: "",
        cast = emptyList(),
        director = "",
        mediaType = result.mediaType ?: "",
        releaseDate = result.releaseDate ?: "",
        duration = "",
        rating = 0.0,
        voteCount = 0,
        language = "",
        country = "",
        trailerUrl = "",
        availableOn = emptyList()
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                sharedViewModel.selectItem(item)
                navController.navigate(Routes.DETAIL)
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(posterUrl),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.mediaType,
                style = MaterialTheme.typography.bodySmall
            )
        }

        IconButton(onClick = { /* TODO */ }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More")
        }
    }
}