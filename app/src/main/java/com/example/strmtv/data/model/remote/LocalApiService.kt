package com.example.strmtv.data.model.remote

import com.example.strmtv.data.model.Item
import retrofit2.http.GET
import retrofit2.http.Query

interface LocalApiService {
    @GET("/api/search")
    suspend fun searchLocal(@Query("query") query: String): List<Item>

    @GET("/api/items")
    suspend fun getItems(): List<Item>
}