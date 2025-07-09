package com.example.movieapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.movieapp.data.local.MovieEntity
import com.example.movieapp.data.repository.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val repo: MovieRepository) : ViewModel() {
    private val _results = MutableLiveData<List<MovieEntity>>()
    val results: LiveData<List<MovieEntity>> = _results
    private var searchJob: Job? = null
    fun search(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            _results.value = if (query.isNotBlank()) repo.search(query) else emptyList()
        }
    }
}
