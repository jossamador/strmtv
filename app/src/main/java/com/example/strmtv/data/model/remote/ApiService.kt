package com.example.strmtv.data.remote

import com.example.strmtv.data.model.Item
import retrofit2.http.GET

interface ApiService {
    @GET("/api/items")
    suspend fun getItems(): List<Item>
}