package com.example.movieapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.movieapp.data.local.BookMarkedMovieEntity
import com.example.movieapp.data.local.MovieEntity
import com.example.movieapp.data.repository.MovieRepository
import kotlinx.coroutines.launch

class SavedViewModel(private val repo: MovieRepository) : ViewModel() {
    private val _saved = MutableLiveData<List<BookMarkedMovieEntity>>()
    val saved: LiveData<List<BookMarkedMovieEntity>> = _saved
    fun loadSaved() = viewModelScope.launch { _saved.value = repo.getBookmarked() }
}
