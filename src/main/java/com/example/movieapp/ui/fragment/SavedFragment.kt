package com.example.movieapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.FragmentSavedBinding
import com.example.movieapp.data.remote.ApiClient
import com.example.movieapp.data.repository.MovieRepository
import com.example.movieapp.App
import com.example.movieapp.ui.adapter.BookMarkMovieAdapter
import com.example.movieapp.ui.adapter.MovieAdapter
import com.example.movieapp.ui.viewmodel.SavedViewModel
import com.example.movieapp.ui.viewmodel.SavedViewModelFactory

class SavedFragment : Fragment() {
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        val repo = MovieRepository(ApiClient.tmdbApi, App.database.movieDao(), context, App.database.bookMarkedMovieDao())
        ViewModelProvider(this, SavedViewModelFactory(repo)).get(SavedViewModel::class.java)
    }
    private lateinit var adapter: BookMarkMovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentSavedBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = BookMarkMovieAdapter { movie ->
            findNavController().navigate(HomeFragmentDirections.actionHomeToDetails(movie.id))
        }
        binding.rvSaved.layoutManager = LinearLayoutManager(context)
        binding.rvSaved.adapter = adapter
        viewModel.saved.observe(viewLifecycleOwner) { adapter.submitList(it) }
        viewModel.loadSaved()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
