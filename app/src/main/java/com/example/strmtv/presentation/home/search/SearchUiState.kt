// ✅ 1. SearchUiState.kt
package com.example.strmtv.presentation.home.search

import com.example.strmtv.data.model.Item

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val results: List<Item>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}