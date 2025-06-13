package com.example.strmtv.presentation.home

import androidx.lifecycle.ViewModel
import com.example.strmtv.data.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel : ViewModel() {

    private val _selectedItem = MutableStateFlow<Item?>(null)
    val selectedItem: StateFlow<Item?> = _selectedItem

    fun selectItem(item: Item) {
        _selectedItem.value = item
    }
}