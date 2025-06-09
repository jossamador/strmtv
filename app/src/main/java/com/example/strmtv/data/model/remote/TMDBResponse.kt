package com.example.strmtv.data.model.remote

import com.google.gson.annotations.SerializedName

data class TMDBResponse(
    @SerializedName("results")
    val results: List<TMDBItem>
)