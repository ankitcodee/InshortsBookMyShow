package com.example.movieapp.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentDetailsBinding
import com.example.movieapp.data.remote.ApiClient
import com.example.movieapp.data.repository.MovieRepository
import com.example.movieapp.App
import com.example.movieapp.data.local.BookMarkedMovieEntity
import com.example.movieapp.ui.viewmodel.DetailsViewModel
import com.example.movieapp.ui.viewmodel.DetailsViewModelFactory
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialSharedAxis

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel by lazy {
        val repo = MovieRepository(ApiClient.tmdbApi, App.database.movieDao(), context, App.database.bookMarkedMovieDao())
        ViewModelProvider(this, DetailsViewModelFactory(repo)).get(DetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment; duration = 400; scrimColor = Color.TRANSPARENT
            fadeMode = MaterialContainerTransform.FADE_MODE_OUT
        }
        sharedElementReturnTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment; duration = 300; scrimColor = Color.TRANSPARENT
            fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply { duration = 300 }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply { duration = 200 }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDetailsBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.movie.observe(viewLifecycleOwner) { movie ->
            binding.collapsingLayout.title = movie.title
            binding.tvTitle.text = movie.title; binding.tvOverview.text = movie.overview
            Glide.with(this).load("https://image.tmdb.org/t/p/w780${movie.posterPath}").into(binding.ivBackdrop)
            val icon = if (movie.bookmarked) R.drawable.bookmark_filled else R.drawable.bookmark_empty
            binding.btnBookmark.setImageResource(icon)
            binding.btnBookmark.setOnClickListener {
                val entity = BookMarkedMovieEntity(movie.id, movie.title, movie.posterPath, movie.overview)
                if(movie.bookmarked) viewModel.removeBookMarkedMovie(entity) else viewModel.addToBookMarkedMovie(entity)
                val icon = if (!movie.bookmarked) R.drawable.bookmark_filled else R.drawable.bookmark_empty
                binding.btnBookmark.setImageResource(icon)}
            binding.btnShare.setOnClickListener {
                val link = "movieapp://movie/${movie.id}"
                val intent = Intent().apply { action = Intent.ACTION_SEND; putExtra(Intent.EXTRA_TEXT, "Check out this movie: $link"); type = "text/plain" }
                startActivity(Intent.createChooser(intent, "Share via"))
            }
        }
        viewModel.load(args.movieId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
