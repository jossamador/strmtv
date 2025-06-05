package com.example.strmtv.data.repository

import com.example.strmtv.data.remote.ApiService
import com.example.strmtv.data.model.Item
import com.example.strmtv.domain.repository.ItemRepository
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ItemRepository {
    override suspend fun getItems(): List<Item> {
        return apiService.getItems()
    }
}