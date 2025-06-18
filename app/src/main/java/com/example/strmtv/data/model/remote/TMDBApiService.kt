package com.example.strmtv.data.model.remote

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): TMDBResponse

    @GET("{media_type}/{media_id}/recommendations")
    suspend fun getRecommendations(
        @Path("media_type") mediaType: String,
        @Path("media_id") mediaId: Int,
        @Query("api_key") apiKey: String
    ): TMDBRecommendationResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): VideoResponse

    @GET("tv/{tv_id}/videos")
    suspend fun getTVShowVideos(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): VideoResponse

    @GET("movie/{movie_id}")
    suspend fun getRawMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): JsonObject

    @GET("tv/{tv_id}")
    suspend fun getRawTVDetails(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): JsonObject

    @GET("{media_type}/{media_id}/credits")
    suspend fun getCredits(
        @Path("media_type") mediaType: String,
        @Path("media_id") mediaId: Int,
        @Query("api_key") apiKey: String
    ): JsonObject

    @GET("movie/{movie_id}/credits")
    suspend fun getRawMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): JsonObject
}

data class VideoResponse(
    val results: List<VideoResult>
)

data class VideoResult(
    val key: String,
    val site: String,
    val type: String
)