package com.example.strmtv.data.model.remote

import com.google.gson.annotations.SerializedName

data class TMDBItem(
    @SerializedName("title")
    val title: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("media_type")
    val mediaType: String? = null,

    @SerializedName("overview")
    val overview: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null
)