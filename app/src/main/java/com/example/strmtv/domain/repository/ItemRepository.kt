package com.example.strmtv.domain.repository

import com.example.strmtv.data.model.Item
import com.example.strmtv.data.model.remote.TMDBItem

interface ItemRepository {

    suspend fun getItems(): List<Item>

    suspend fun searchLocal(query: String): List<Item>

    suspend fun searchTMDB(query: String): List<Item>

    suspend fun getRecommendationsFromTMDB(mediaType: String, mediaId: Int): List<TMDBItem>
}