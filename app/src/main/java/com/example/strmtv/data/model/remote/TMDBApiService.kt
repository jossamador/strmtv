package com.example.strmtv.data.model.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApiService {

    @GET("/api/search_tmdb")
    suspend fun searchTMDB(@Query("query") query: String): TMDBResponse
}