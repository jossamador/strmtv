package com.example.strmtv.presentation.home.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.example.strmtv.data.model.Item
import com.example.strmtv.data.model.remote.TMDBItem
import com.example.strmtv.presentation.home.SharedViewModel
import com.example.strmtv.presentation.home.navigation.Routes

@Composable
fun ResultItemLocal(
    result: Item,
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable {
                sharedViewModel.selectItem(result)
                navController.navigate(Routes.DETAIL)
            },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = result.poster,
                contentDescription = result.title ?: "Poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = result.title ?: "Sin título",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = result.genres.joinToString(", ").ifBlank { "Género desconocido" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = result.year?.toString() ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
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
    val subtitle = when (result.mediaType) {
        "movie" -> "Movie"
        "tv" -> "TV Show"
        "person" -> "Cast & Crew"
        else -> "Unknown"
    }

    val posterPath = result.posterPath?.removePrefix("/") ?: ""
    val posterUrl = "https://image.tmdb.org/t/p/w500/$posterPath"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable {
                sharedViewModel.selectItem(
                    Item(
                        id = result.id ?: -1,
                        title = result.title ?: result.name ?: "Sin título",
                        poster = posterUrl,
                        backdrop = result.backdropPath?.let {
                            "https://image.tmdb.org/t/p/w780/${it.removePrefix("/")}"
                        } ?: "",
                        releaseDate = result.releaseDate ?: "",
                        overview = result.overview ?: "",
                        genres = emptyList(),
                        genre = "",
                        director = "",
                        cast = emptyList(),
                        rating = result.voteAverage ?: 0.0,
                        voteCount = result.voteCount ?: 0,
                        duration = "",
                        language = result.originalLanguage ?: "",
                        country = "",
                        availableOn = emptyList(),
                        trailerUrl = "",
                        type = result.mediaType ?: "",
                        mediaType = result.mediaType ?: "",
                        year = result.releaseDate?.takeIf { it.isNotBlank() }?.take(4)?.toIntOrNull() ?: 0
                    )
                )
                navController.navigate(Routes.DETAIL)
            },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = posterUrl,
                contentDescription = result.title ?: result.name ?: "Poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = result.title ?: result.name ?: "Sin título",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = result.releaseDate ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

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
        TextField(
            value = query,
            onValueChange = {
                query = it
                searchViewModel.performSearch(it)
            },
            label = { Text("Buscar en TMDB o Local") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { searchViewModel.setUseLocalSource(true) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (useLocalSource) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (useLocalSource) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Buscar en Local")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { searchViewModel.setUseLocalSource(false) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!useLocalSource) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (!useLocalSource) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Buscar en TMDB")
            }
        }

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        searchViewModel.selectedCategory = title
                        searchViewModel.performSearch(query)
                    },
                    text = {
                        Text(
                            title,
                            color = if (selectedTabIndex == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (uiState) {
            is SearchUiState.Idle -> {}
            is SearchUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is SearchUiState.SuccessLocal -> {
                val filtered = searchViewModel.getFilteredResultsByTab(selectedTabIndex)
                ResultsListLocal(
                    results = (uiState as SearchUiState.SuccessLocal).results,
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
            is SearchUiState.SuccessTMDB -> {
                val filtered = searchViewModel.getFilteredTMDBResultsByTab(selectedTabIndex)
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
            ResultItemLocal(result, navController, sharedViewModel)
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
            ResultItemTMDB(result, navController, sharedViewModel)
        }
    }
}
