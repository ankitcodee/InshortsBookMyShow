package com.example.movieapp.data.repository

import android.content.Context
import android.util.Log
import com.example.movieapp.data.local.BookMarkedMovieDao
import com.example.movieapp.data.local.BookMarkedMovieEntity
import com.example.movieapp.data.local.MovieDao
import com.example.movieapp.data.local.MovieEntity
import com.example.movieapp.data.remote.TmdbApi
import com.example.movieapp.utils.NetworkUtils

class MovieRepository(private val api: TmdbApi, private val dao: MovieDao, context: Context?, private val bookmarkDao : BookMarkedMovieDao) {
    suspend fun getTrending(): List<MovieEntity> {
        try {
            val movies = api.getTrending().results.map { it.toEntity() }
            dao.insertMovies(movies)
            return movies
        } catch (e: Exception) {
            Log.e(TAG, "Exception while fetching data from server : ${e.message}")
            return dao.getAllMovies()
        }
    }

    suspend fun getNowPlaying(): List<MovieEntity> {
        try {
            val movies = api.getNowPlaying().results.map { it.toEntity() }
            dao.insertMovies(movies)
            return movies
        } catch (e: Exception) {
            Log.e(TAG, "Exception while fetching data from server : ${e.message}")
            return dao.getAllMovies()
        }
    }

    suspend fun search(query: String): List<MovieEntity> {
        return try {
            val response = api.searchMovies(query)
            response.results.map { it.toEntity() }
        } catch (e: Exception) {
            Log.e("MovieRepository", "Search failed: ${e.message}")
            emptyList()
        }
    }

    suspend fun getBookmarked() = bookmarkDao.getAllBookMarkedMovies()
    suspend fun toggleBookmark(movie: BookMarkedMovieEntity) =
        bookmarkDao.insertBookMarkedMovie(movie)

    suspend fun addToBookMark(movie : BookMarkedMovieEntity) {
        bookmarkDao.insertBookMarkedMovie(movie)
    }

    suspend fun removeFromBookMark(movie: BookMarkedMovieEntity) {
        bookmarkDao.deleteBookmarkedMovie(movie)
    }

    companion object {
        const val TAG = "MovieRepository"
    }
}

private fun com.example.movieapp.data.model.Movie.toEntity() =
    MovieEntity(id, title, posterPath, overview)