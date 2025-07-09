package com.example.movieapp.data.local

import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieEntity>
    @Query("SELECT * FROM movies WHERE bookmarked = true")
    suspend fun getBookmarked(): List<MovieEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)
    @Update
    suspend fun updateMovie(movie: MovieEntity)
}
