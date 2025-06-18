package com.example.strmtv.presentation.home.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.strmtv.data.model.Item
import com.example.strmtv.data.model.remote.TMDBItem
import com.example.strmtv.presentation.home.SharedViewModel
import com.example.strmtv.presentation.home.navigation.Routes

@Composable
fun SearchScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val query by remember { derivedStateOf { searchViewModel.currentQuery } }
    val uiState by searchViewModel.uiState.collectAsState()
    val useLocalSource by searchViewModel.useLocalSource.collectAsState()

    val tabs = listOf("Top Results", "TV Shows", "Movies", "Cast & Crew")
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(query, useLocalSource) {
        if (query.isNotBlank()) {
            searchViewModel.performSearch(query)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF121212), Color(0xFF1A1A1A))))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            EnhancedSearchHeader(
                query = query,
                onQueryChange = {
                    searchViewModel.updateQuery(it)
                    if (it.isNotBlank()) {
                        searchViewModel.performSearch(it)
                    }
                },
                useLocalSource = useLocalSource,
                onSourceChange = {
                    searchViewModel.setUseLocalSource(it)
                    if (query.isNotBlank()) {
                        searchViewModel.performSearch(query)
                    }
                },
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index ->
                    selectedTabIndex = index
                    searchViewModel.selectedCategory = tabs.getOrNull(index) ?: ""
                    if (query.isNotBlank()) {
                        searchViewModel.performSearch(query)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (uiState) {
                is SearchUiState.Idle -> {}
                is SearchUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }
                is SearchUiState.Success -> {
                    val filtered = (uiState as SearchUiState.Success).results
                        .filterByCategory(searchViewModel.selectedCategory)
                    ResultsList(filtered, navController, sharedViewModel)
                }
                is SearchUiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text(text = (uiState as SearchUiState.Error).message)
                }
            }
        }
    }
}

@Composable
fun EnhancedSearchHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    useLocalSource: Boolean,
    onSourceChange: (Boolean) -> Unit,
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("TV Shows, Movies, Cast & Crew...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color.Gray
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color(0xFF1C1C1E),
                unfocusedContainerColor = Color(0xFF1C1C1E),
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            listOf(true to "Buscar en Local", false to "Buscar en TMDB").forEach { (isLocal, label) ->
                val selected = useLocalSource == isLocal
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (selected) Color(0xFF007AFF) else Color.Transparent)
                        .border(
                            width = 1.dp,
                            color = if (selected) Color(0xFF007AFF) else MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .clickable { onSourceChange(isLocal) }
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (selected) Color.White else Color.LightGray,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) Color(0xFF007AFF) else Color.Transparent)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Color(0xFF007AFF) else Color.Gray,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onTabSelected(index) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) Color.White else Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ResultsList(
    results: List<Item>,
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        items(results) { result ->
            ResultItem(result = result, navController = navController, sharedViewModel = sharedViewModel)
        }
    }
}

@Composable
fun ResultItem(
    result: Item,
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val isPerson = result.mediaType == "person"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable {
                sharedViewModel.selectItem(result)
                navController.navigate(Routes.DETAIL)
            },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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

                if (!isPerson) {
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
                } else {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Persona",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

fun <T> List<T>.filterByCategory(category: String): List<T> {
    return when (category) {
        "TV Shows" -> filter {
            (it as? Item)?.mediaType == "tv" || (it as? TMDBItem)?.mediaType == "tv"
        }
        "Movies" -> filter {
            (it as? Item)?.mediaType == "movie" || (it as? TMDBItem)?.mediaType == "movie"
        }
        "Cast & Crew" -> filter {
            (it as? Item)?.mediaType == "person" || (it as? TMDBItem)?.mediaType == "person"
        }
        else -> this
    }
}