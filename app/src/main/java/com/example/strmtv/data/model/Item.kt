package com.example.strmtv.data.model

data class Item(
    val id: Int,
    val title: String,
    val type: String,
    val year: Int,
    val genre: String,
    val genres: List<String>,
    val poster: String,
    val backdrop: String,
    val overview: String,
    val cast: List<String>,
    val director: String,
    val mediaType: String,  // (entiendo que es "movie" o "series")
    val releaseDate: String,
    val duration: String,
    val rating: Double,
    val voteCount: Int,
    val language: String,
    val country: String,
    val trailerUrl: String,
    val availableOn: List<String>
)