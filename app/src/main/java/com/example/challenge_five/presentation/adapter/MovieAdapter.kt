package com.example.challenge_five.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.challenge_five.R
import com.example.challenge_five.common.Constants.BASE_IMAGE_URL
import com.example.challenge_five.databinding.ItemContainerBinding
import com.example.challenge_five.domain.model.Result
import com.example.challenge_five.presentation.ui.movie_list.MovieListFragmentDirections

class MovieAdapter(private val listMovie: List<Result>?, private val userId: Int) :
    ListAdapter<Result, MovieAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Result) {
            binding.apply {
                titleTextView.text = movie.title
                ratingTextView.text = movie.voteAverage.toString()
                popularityTextView.text = movie.popularity.toString()
                Glide.with(itemView)
                    .load(BASE_IMAGE_URL + movie.posterPath)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(binding.posterImageView)
                itemContainer.setOnClickListener {
                    val action =
                        MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(
                            id = movie.id, userId = userId, movie = movie
                        )
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listMovie?.get(position)!!)
    }

    override fun getItemCount(): Int = listMovie!!.size

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(
                oldItem: Result,
                newItem: Result
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Result,
                newItem: Result
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}