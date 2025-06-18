package com.example.strmtv.data.model.repository

import com.example.strmtv.data.model.Item
import com.example.strmtv.data.model.remote.LocalApiService
import com.example.strmtv.data.model.remote.TMDBApiService
import com.example.strmtv.data.model.remote.TMDBItem
import com.example.strmtv.domain.repository.ItemRepository
import javax.inject.Inject
import com.google.gson.JsonObject

class ItemRepositoryImpl @Inject constructor(
    private val tmdbApiService: TMDBApiService,
    private val localApiService: LocalApiService
) : ItemRepository {

    override suspend fun searchLocal(query: String): List<Item> {
        return try {
            localApiService.getItems().filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.genre.contains(query, ignoreCase = true) ||
                        it.genres.any { genre -> genre.contains(query, ignoreCase = true) }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun searchTMDB(query: String): List<Item> {
        println("🔵 LLAMANDO A TMDB con query: $query")
        return try {
            val response = tmdbApiService.searchMulti(query, TMDB_API_KEY)
            response.results.mapNotNull { tmdbItem ->
                val id = tmdbItem.id ?: return@mapNotNull null
                val title = tmdbItem.title ?: tmdbItem.name ?: return@mapNotNull null
                val mediaType = tmdbItem.mediaType ?: "unknown"

                if (mediaType == "person") {
                    return@mapNotNull Item(
                        id = id,
                        title = title,
                        poster = tmdbItem.profilePath?.let { "https://image.tmdb.org/t/p/w500/${it.removePrefix("/")}" } ?: "",
                        backdrop = "",
                        releaseDate = "",
                        overview = "",
                        genres = emptyList(),
                        genre = "",
                        director = "",
                        cast = emptyList(),
                        rating = 0.0,
                        voteCount = 0,
                        duration = "",
                        language = "",
                        country = "",
                        availableOn = emptyList(),
                        trailerUrl = "",
                        type = mediaType,
                        mediaType = mediaType,
                        year = 0
                    )
                }

                // Tráiler
                val trailerUrl = try {
                    val videoResponse = when (mediaType) {
                        "movie" -> tmdbApiService.getMovieVideos(id, TMDB_API_KEY)
                        "tv" -> tmdbApiService.getTVShowVideos(id, TMDB_API_KEY)
                        else -> null
                    }
                    val trailerKey = videoResponse?.results?.firstOrNull {
                        it.site == "YouTube" && it.type == "Trailer"
                    }?.key
                    trailerKey?.let { "https://www.youtube.com/watch?v=$it" } ?: ""
                } catch (e: Exception) {
                    ""
                }

                // Duración y país
                val (duration, country) = try {
                    val detailsJson = when (mediaType) {
                        "movie" -> tmdbApiService.getRawMovieDetails(id, TMDB_API_KEY)
                        "tv" -> tmdbApiService.getRawTVDetails(id, TMDB_API_KEY)
                        else -> null
                    }
                    val durationStr = detailsJson?.get("runtime")?.asInt?.toString() ?: ""
                    val countryStr = detailsJson
                        ?.getAsJsonArray("production_countries")
                        ?.firstOrNull()?.asJsonObject?.get("name")?.asString ?: ""
                    durationStr to countryStr
                } catch (e: Exception) {
                    "" to ""
                }

                // Director
                val director = try {
                    val creditsJson = tmdbApiService.getCredits(mediaType, id, TMDB_API_KEY)

                    creditsJson.getAsJsonArray("crew")
                        ?.firstOrNull { it.asJsonObject["job"]?.asString == "Director" }
                        ?.asJsonObject?.get("name")?.asString ?: ""
                } catch (e: Exception) {
                    ""
                }

                // Construir objeto Item
                Item(
                    id = id,
                    title = title,
                    poster = tmdbItem.posterPath?.let { "https://image.tmdb.org/t/p/w500/${it.removePrefix("/")}" } ?: "",
                    backdrop = tmdbItem.backdropPath?.let { "https://image.tmdb.org/t/p/w780/${it.removePrefix("/")}" } ?: "",
                    releaseDate = tmdbItem.releaseDate ?: "",
                    overview = tmdbItem.overview ?: "",
                    genres = emptyList(),
                    genre = "",
                    director = director,
                    cast = emptyList(),
                    rating = tmdbItem.voteAverage ?: 0.0,
                    voteCount = tmdbItem.voteCount ?: 0,
                    duration = duration,
                    language = tmdbItem.originalLanguage ?: "",
                    country = country,
                    availableOn = emptyList(),
                    trailerUrl = trailerUrl,
                    type = mediaType,
                    mediaType = mediaType,
                    year = tmdbItem.releaseDate?.take(4)?.toIntOrNull() ?: 0
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getRecommendationsFromTMDB(mediaType: String, mediaId: Int): List<TMDBItem> {
        return try {
            val response = tmdbApiService.getRecommendations(mediaType, mediaId, TMDB_API_KEY)
            response.results
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun getItems(): List<Item> {
        return try {
            localApiService.getItems()
        } catch (_: Exception) {
            emptyList()
        }
    }

    companion object {
        const val TMDB_API_KEY = "1087b47ed60a7c149fc4101df16584ed"
    }

    suspend fun getMovieRawDetails(id: Int): JsonObject? {
        return try {
            tmdbApiService.getRawMovieDetails(id, TMDB_API_KEY)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getMovieRawCredits(id: Int): JsonObject? {
        return try {
            tmdbApiService.getRawMovieCredits(id, TMDB_API_KEY)
        } catch (e: Exception) {
            null
        }
    }
}