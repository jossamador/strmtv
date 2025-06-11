package com.example.strmtv.presentation.home.search

import com.example.strmtv.data.model.Item
import com.example.strmtv.data.model.remote.TMDBItem

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class SuccessLocal(val results: List<Item>) : SearchUiState()
    data class SuccessTMDB(val results: List<TMDBItem>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}