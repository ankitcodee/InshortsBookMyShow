package com.example.movieapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("trending/movie/day")
    suspend fun getTrending(): ApiResponse

    @GET("movie/now_playing")
    suspend fun getNowPlaying(): ApiResponse

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String): ApiResponse
}
