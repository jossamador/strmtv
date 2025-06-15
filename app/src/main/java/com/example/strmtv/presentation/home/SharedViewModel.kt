package com.example.strmtv.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strmtv.data.model.Item
import com.example.strmtv.data.model.remote.TMDBItem
import com.example.strmtv.domain.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- UI-state para recomendaciones ---
sealed class RecommendationsUiState {
    object Idle : RecommendationsUiState()
    object Loading : RecommendationsUiState()
    data class Success(val data: List<TMDBItem>) : RecommendationsUiState()
    data class Error(val message: String) : RecommendationsUiState()
}

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _selectedItem = MutableStateFlow<Item?>(null)
    val selectedItem: StateFlow<Item?> = _selectedItem

    private val _recoState = MutableStateFlow<RecommendationsUiState>(RecommendationsUiState.Idle)
    val recoState: StateFlow<RecommendationsUiState> = _recoState

    fun selectItem(item: Item) {
        _selectedItem.value = item

        if (item.id != 0 && !item.mediaType.isNullOrBlank()) {
            loadRecommendations(item)
        } else {
            _recoState.value = RecommendationsUiState.Success(emptyList())
        }
    }

    private fun loadRecommendations(item: Item) = viewModelScope.launch {
        if (item.id == null || item.id == 0 || item.mediaType.isNullOrBlank()) {
            Log.d("TMDB_RECO", "Item inválido para recomendaciones. Se ignora.")
            _recoState.value = RecommendationsUiState.Idle
            return@launch
        }

        _recoState.value = RecommendationsUiState.Loading
        try {
            val list = repository.getRecommendationsFromTMDB(item.mediaType, item.id)
            _recoState.value = RecommendationsUiState.Success(list)
        } catch (e: Exception) {
            _recoState.value = RecommendationsUiState.Error(e.message ?: "Error desconocido")
        }
    }
}