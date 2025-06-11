package com.example.strmtv.presentation.home.search

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

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _useLocalSource = MutableStateFlow(true)
    val useLocalSource: StateFlow<Boolean> = _useLocalSource.asStateFlow()

    fun setUseLocalSource(useLocal: Boolean) {
        _useLocalSource.value = useLocal
    }

    fun search(query: String) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            try {
                if (useLocalSource.value) {
                    val results: List<Item> = repository.searchLocal(query)
                    _uiState.value = SearchUiState.SuccessLocal(results)
                } else {
                    val results: List<TMDBItem> = repository.searchTMDB(query)
                    _uiState.value = SearchUiState.SuccessTMDB(results)
                }
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error("Error: ${e.message}")
            }
        }
    }
}