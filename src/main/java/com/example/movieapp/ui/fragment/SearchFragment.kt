package com.example.movieapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.data.remote.ApiClient
import com.example.movieapp.data.repository.MovieRepository
import com.example.movieapp.App
import com.example.movieapp.ui.adapter.MovieAdapter
import com.example.movieapp.ui.viewmodel.SearchViewModel
import com.example.movieapp.ui.viewmodel.SearchViewModelFactory

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        val repo = MovieRepository(ApiClient.tmdbApi, App.database.movieDao(), context, App.database.bookMarkedMovieDao())
        ViewModelProvider(this, SearchViewModelFactory(repo)).get(SearchViewModel::class.java)
    }
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentSearchBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = MovieAdapter { movie ->
            findNavController().navigate(SearchFragmentDirections.actionSearchToDetails(movie.id))
        }
        binding.rvSearchResults.layoutManager = LinearLayoutManager(context)
        binding.rvSearchResults.adapter = adapter
        viewModel.results.observe(viewLifecycleOwner) { adapter.submitList(it) }
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText.orEmpty()); return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
