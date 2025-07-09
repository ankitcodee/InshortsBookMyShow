package com.example.movieapp.data.model

import com.squareup.moshi.Json

data class Movie(
    val id: Int,
    val title: String,
    @Json(name = "poster_path") val posterPath: String?,
    val overview: String
)