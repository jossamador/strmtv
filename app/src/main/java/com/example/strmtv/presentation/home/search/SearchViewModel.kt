package com.example.strmtv.presentation.home.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strmtv.data.model.Item
import com.example.strmtv.data.model.remote.TMDBItem
import com.example.strmtv.domain.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState

    private val _useLocalSource = MutableStateFlow(true)
    val useLocalSource: StateFlow<Boolean> = _useLocalSource

    var selectedCategory by mutableStateOf("All") // o "Top Results", "TV Shows", etc.

    fun setUseLocalSource(value: Boolean) {
        _useLocalSource.value = value
    }


    fun toggleSource() {
        _useLocalSource.value = !_useLocalSource.value
    }

    fun performSearch(query: String) {
        viewModelScope.launch {
            try {
                _uiState.value = SearchUiState.Loading

                if (useLocalSource.value) {
                    val results = repository.searchLocal(query)
                        .filterByCategory(selectedCategory)
                    _uiState.value = SearchUiState.SuccessLocal(results)
                } else {
                    val results = repository.searchTMDB(query)
                        .filterByCategory(selectedCategory)
                    _uiState.value = SearchUiState.SuccessTMDB(results)
                }

            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun getFilteredResultsByTab(tabIndex: Int): List<Item> {
        val allItems = (_uiState.value as? SearchUiState.SuccessLocal)?.results ?: return emptyList()
        return when (tabIndex) {
            0 -> allItems // Top Results (sin filtro)
            1 -> allItems.filter { it.mediaType == "tv" }
            2 -> allItems.filter { it.mediaType == "movie" }
            3 -> allItems.filter { it.mediaType == "person" } // si lo tienes, opcional
            else -> allItems
        }
    }

    fun getFilteredTMDBResultsByTab(tabIndex: Int): List<TMDBItem> {
        val allResults = (_uiState.value as? SearchUiState.SuccessTMDB)?.results ?: return emptyList()
        return when (tabIndex) {
            0 -> allResults
            1 -> allResults.filter { it.mediaType == "tv" }
            2 -> allResults.filter { it.mediaType == "movie" }
            3 -> allResults.filter { it.mediaType == "person" }
            else -> allResults
        }
    }

    private fun searchLocal(query: String) = viewModelScope.launch {
        try {
            println("🔍 Buscando en Local: $query")
            val results = repository.searchLocal(query)
            _uiState.value = SearchUiState.SuccessLocal(results)
        } catch (e: Exception) {
            println("❌ Error en búsqueda local: ${e.message}")
            _uiState.value = SearchUiState.Error("Error: ${e.message}")
        }
    }

    private fun searchTMDB(query: String) = viewModelScope.launch {
        try {
            println("🔍 Buscando en TMDB: $query")
            val results = repository.searchTMDB(query)
            println("✅ TMDB Resultados obtenidos: ${results.size}")
            _uiState.value = SearchUiState.SuccessTMDB(results)
        } catch (e: Exception) {
            println("❌ Error en búsqueda TMDB: ${e.message}")
            _uiState.value = SearchUiState.Error("Error: ${e.message}")
        }
    }
}

private fun <T> List<T>.filterByCategory(category: String): List<T> {
    return when (category) {
        "TV Shows" -> filter {
            when (it) {
                is TMDBItem -> it.mediaType == "tv"
                is Item -> it.mediaType == "tv"
                else -> true
            }
        }
        "Movies" -> filter {
            when (it) {
                is TMDBItem -> it.mediaType == "movie"
                is Item -> it.mediaType == "movie"
                else -> true
            }
        }
        "Cast & Crew" -> filter {
            when (it) {
                is TMDBItem -> it.mediaType == "person"
                else -> false
            }
        }
        else -> this // "Top Results" or unknown
    }
}