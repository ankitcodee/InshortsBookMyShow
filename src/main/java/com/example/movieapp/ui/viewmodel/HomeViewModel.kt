package com.example.movieapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.movieapp.data.local.MovieEntity
import com.example.movieapp.data.repository.MovieRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: MovieRepository) : ViewModel() {
    private val _trending = MutableLiveData<List<MovieEntity>>()
    val trending: LiveData<List<MovieEntity>> = _trending
    private val _nowPlaying = MutableLiveData<List<MovieEntity>>()
    val nowPlaying: LiveData<List<MovieEntity>> = _nowPlaying
    fun loadMovies() = viewModelScope.launch {
        _trending.value = repo.getTrending()
        _nowPlaying.value = repo.getNowPlaying()
    }
}
