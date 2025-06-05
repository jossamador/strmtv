package com.example.strmtv.data.model

data class Item(
    val id: Int,
    val title: String,
    val type: String, // puede ser "movie" o "series"
    val year: Int,
    val genre: String,
    val seasons: Int?, // solo para series, puede ser null
    val poster: String // URL de la imagen
)