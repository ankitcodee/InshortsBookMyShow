package com.example.movieapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.data.remote.ApiClient
import com.example.movieapp.data.repository.MovieRepository
import com.example.movieapp.App
import com.example.movieapp.ui.adapter.MovieAdapter
import com.example.movieapp.ui.viewmodel.HomeViewModel
import com.example.movieapp.ui.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        val repo = MovieRepository(ApiClient.tmdbApi, App.database.movieDao(), context, App.database.bookMarkedMovieDao())
        ViewModelProvider(this, HomeViewModelFactory(repo)).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentHomeBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val trendingAdapter = MovieAdapter { movie ->
            findNavController().navigate(HomeFragmentDirections.actionHomeToDetails(movie.id))
        }

        binding.searchButton.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment()) }

        binding.bookmarkButton.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSavedFragment()) }
        binding.rvTrending.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTrending.adapter = trendingAdapter

        val nowAdapter = MovieAdapter { movie ->
            findNavController().navigate(HomeFragmentDirections.actionHomeToDetails(movie.id))
        }
        binding.rvNowPlaying.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvNowPlaying.adapter = nowAdapter

        viewModel.trending.observe(viewLifecycleOwner) { trendingAdapter.submitList(it) }
        viewModel.nowPlaying.observe(viewLifecycleOwner) { nowAdapter.submitList(it) }
        viewModel.loadMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
