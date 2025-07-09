package com.example.movieapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookMarkedMovieDao {
    @Query("Select * from bookmarked_movies")
    suspend fun getAllBookMarkedMovies() : List<BookMarkedMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookMarkedMovie(movie: BookMarkedMovieEntity)

    @Update
    suspend fun updateBookMarkedMovie(movie: BookMarkedMovieEntity)

    @Delete
    suspend fun deleteBookmarkedMovie(movie: BookMarkedMovieEntity)
}