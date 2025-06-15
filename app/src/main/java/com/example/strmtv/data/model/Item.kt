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
    val overview: String?,              // <- puede ser null
    val cast: List<String>,
    val director: String?,              // <- puede ser null
    val mediaType: String,
    val releaseDate: String?,           // <- puede ser null
    val duration: String?,              // <- ya lo tenías nullable 👍
    val rating: Double,
    val voteCount: Int,
    val language: String?,              // <- puede ser null
    val country: String?,               // <- puede ser null
    val trailerUrl: String?,            // <- puede ser null
    val availableOn: List<String>
)