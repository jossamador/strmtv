package com.example.strmtv.presentation.home


import com.example.strmtv.data.model.Item

sealed class UiState {
    object Loading : UiState()
    data class Success(val items: List<Item>) : UiState()
    data class Error(val message: String) : UiState()
}