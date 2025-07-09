package com.example.movieapp.ui.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.movieapp.data.local.BookMarkedMovieEntity
import com.example.movieapp.data.local.MovieEntity
import com.example.movieapp.data.repository.MovieRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailsViewModel(private val repo: MovieRepository) : ViewModel() {
    private val _movie = MutableLiveData<MovieEntity>()
    val movie: LiveData<MovieEntity> = _movie
    fun load(id: Int) = viewModelScope.launch {
        var list: List<MovieEntity>? = null
        try {
            list = repo.getTrending() + repo.getNowPlaying()
        } catch (e: HttpException) {
            Log.e(TAG, "No request from server")
        } catch (e: Exception) {
            Log.e(TAG, "Exception : ${e.message}")
        }

        if (list != null) {
            _movie.value = list.first { it.id == id }
        }
    }

    fun addToBookMarkedMovie(entity: BookMarkedMovieEntity) = viewModelScope.launch {
        repo.addToBookMark(entity)
    }

    fun removeBookMarkedMovie(entity: BookMarkedMovieEntity) = viewModelScope.launch {
        repo.removeFromBookMark(entity)
    }

    companion object {
        val TAG = "DetailsViewModel"
    }
}
