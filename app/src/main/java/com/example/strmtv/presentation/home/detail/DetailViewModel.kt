package com.example.strmtv.presentation.home.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strmtv.data.model.remote.TMDBItem
import com.example.strmtv.domain.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    private val _recommendations = MutableStateFlow<List<TMDBItem>>(emptyList())
    val recommendations: StateFlow<List<TMDBItem>> = _recommendations

    fun fetchRecommendations(mediaId: Int, mediaType: String) {
        viewModelScope.launch {
            val result = repository.getRecommendationsFromTMDB(mediaType, mediaId)
            _recommendations.value = result
        }
    }
}