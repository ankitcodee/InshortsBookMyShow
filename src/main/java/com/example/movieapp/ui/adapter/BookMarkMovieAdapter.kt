package com.example.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.local.BookMarkedMovieEntity
import com.example.movieapp.databinding.ItemMovieBinding

class BookMarkMovieAdapter(private val onClick: (BookMarkedMovieEntity) -> Unit) :  ListAdapter<BookMarkedMovieEntity, BookMarkMovieAdapter.MovieViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookMarkMovieAdapter.MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.bind(getItem(position))

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookMarkedMovieEntity>() {
            override fun areItemsTheSame(old: BookMarkedMovieEntity, new: BookMarkedMovieEntity) = old.id == new.id
            override fun areContentsTheSame(old: BookMarkedMovieEntity, new: BookMarkedMovieEntity) = old == new
        }
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: BookMarkedMovieEntity) {
            binding.tvTitleOverlay.text = movie.title
            Glide.with(binding.ivPoster.context)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .placeholder(R.drawable.placeholder)
                .into(binding.ivPoster)
            binding.root.setOnClickListener { onClick(movie) }
        }
    }

}