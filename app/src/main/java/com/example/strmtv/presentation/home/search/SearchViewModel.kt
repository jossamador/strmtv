package com.example.strmtv.presentation.home.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strmtv.data.model.Item
import com.example.strmtv.domain.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState

    private val _useLocalSource = MutableStateFlow(true)
    val useLocalSource: StateFlow<Boolean> = _useLocalSource

    var selectedCategory by mutableStateOf("All")

    var currentQuery by mutableStateOf("")
        private set

    fun updateQuery(newQuery: String) {
        if (currentQuery != newQuery) {
            currentQuery = newQuery
        }
    }

    fun setUseLocalSource(value: Boolean) {
        _useLocalSource.value = value
    }

    fun toggleSource() {
        _useLocalSource.value = !_useLocalSource.value
    }

    fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            try {
                val results = if (useLocalSource.value) {
                    repository.searchLocal(query)
                } else {
                    repository.searchTMDB(query)
                }
                _uiState.value = SearchUiState.Success(results)
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error("Error: ${e.message}")
            }
        }
    }

    // Úsalo solo si lo necesitas directamente desde el ViewModel (no es obligatorio si usas filterByCategory en la UI)
    fun getFilteredResultsByTab(tabIndex: Int): List<Item> {
        val allItems = (uiState.value as? SearchUiState.Success)?.results ?: return emptyList()
        return when (tabIndex) {
            0 -> allItems
            1 -> allItems.filter { it.mediaType == "tv" }
            2 -> allItems.filter { it.mediaType == "movie" }
            3 -> allItems.filter { it.mediaType == "person" }
            else -> allItems
        }
    }
}
