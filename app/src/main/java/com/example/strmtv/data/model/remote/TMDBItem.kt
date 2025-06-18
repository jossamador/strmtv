package com.example.strmtv.data.model.remote

import com.example.strmtv.data.model.Item
import com.google.gson.annotations.SerializedName

fun TMDBItem.toItem(): Item {
    return Item(
        id = this.id ?: -1,
        title = this.title ?: this.name ?: "Sin título",
        poster = this.posterPath?.let { "https://image.tmdb.org/t/p/w500/$it" } ?: "",
        backdrop = this.backdropPath?.let { "https://image.tmdb.org/t/p/w780/$it" } ?: "",
        releaseDate = this.releaseDate ?: "",
        overview = this.overview ?: "",
        genres = emptyList(),
        genre = "",
        director = "",
        cast = emptyList(),
        rating = this.voteAverage ?: 0.0,
        voteCount = this.voteCount ?: 0,
        duration = "",
        language = this.originalLanguage ?: "",
        country = "",
        availableOn = emptyList(),
        trailerUrl = this.trailerUrl ?: "",
        type = this.mediaType ?: "",
        mediaType = this.mediaType ?: "",
        year = this.releaseDate?.take(4)?.toIntOrNull() ?: 0
    )
}

data class TMDBItem(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @SerializedName("profile_path")
    val profilePath: String? = null,

    @SerializedName("media_type")
    val mediaType: String? = null,

    @SerializedName("overview")
    val overview: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    @SerializedName("vote_average")
    val voteAverage: Double? = null,

    @SerializedName("vote_count")
    val voteCount: Int? = null,

    @SerializedName("original_language")
    val originalLanguage: String? = null,

    @SerializedName("trailer_url")
    val trailerUrl: String? = null
)
