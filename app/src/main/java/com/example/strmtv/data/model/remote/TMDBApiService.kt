package com.example.strmtv.data.model.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApiService {
    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): TMDBResponse

    @GET("{media_type}/{media_id}/recommendations")
    suspend fun getRecommendations(
        @retrofit2.http.Path("media_type") mediaType: String, // "movie" o "tv"
        @retrofit2.http.Path("media_id") mediaId: Int,
        @Query("api_key") apiKey: String
    ): TMDBRecommendationResponse

}