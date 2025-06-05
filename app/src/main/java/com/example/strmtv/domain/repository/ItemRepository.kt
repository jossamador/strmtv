package com.example.strmtv.domain.repository

import com.example.strmtv.data.model.Item

interface ItemRepository {
    suspend fun getItems(): List<Item>
}