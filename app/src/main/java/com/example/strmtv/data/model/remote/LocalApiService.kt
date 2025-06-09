package com.example.strmtv.data.model.remote

import com.example.strmtv.data.model.Item
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("items")
    suspend fun getItems(): List<Item>  // ← sigue siendo para la HomeScreen

    @GET("search/multi")
    suspend fun searchTMDB(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): TMDBResponse
}