package com.example.strmtv.data.model.repository

import com.example.strmtv.data.model.Item
import com.example.strmtv.data.model.remote.LocalApiService
import com.example.strmtv.data.model.remote.TMDBApiService
import com.example.strmtv.data.model.remote.TMDBItem
import com.example.strmtv.domain.repository.ItemRepository
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val tmdbApiService: TMDBApiService,
    private val localApiService: LocalApiService
) : ItemRepository {

    // Buscar en Local
    override suspend fun searchLocal(query: String): List<Item> {
        return try {
            localApiService.searchLocal(query)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Buscar en TMDB
    override suspend fun searchTMDB(query: String): List<TMDBItem> {
        return try {
            val response = tmdbApiService.searchMulti(query, TMDB_API_KEY)
            response.results
        } catch (e: Exception) {
            println("Error al buscar en TMDB: ${e.message}")
            emptyList()
        }
    }

    // Implementación de getItems()
    override suspend fun getItems(): List<Item> {
        return try {
            localApiService.getItems()
        } catch (e: Exception) {
            emptyList()
        }
    }

    companion object {
        const val TMDB_API_KEY = "1087b47ed60a7c149fc4101df16584ed"
    }
}