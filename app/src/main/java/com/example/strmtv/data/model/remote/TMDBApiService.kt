package com.example.strmtv.data.model.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApiService {
    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): TMDBResponse
}