package com.example.challenge_five.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.challenge_five.R
import com.example.challenge_five.common.Constants
import com.example.challenge_five.databinding.ItemContainerBinding
import com.example.challenge_five.domain.model.Favorite

class FavoriteAdapter(private val listItem: List<Favorite>?) :
    ListAdapter<Favorite, FavoriteAdapter.ViewHolder>(
        DIFF_CALLBACK
    ) {
    inner class ViewHolder(private val binding: ItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite) {
            binding.apply {
                titleTextView.text = favorite.title
                ratingTextView.text = favorite.voteAverage.toString()
                popularityTextView.text = favorite.popularity.toString()
                Glide.with(itemView)
                    .load(Constants.BASE_IMAGE_URL + favorite.posterPath)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(binding.posterImageView)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listItem!![position])
    }

    override fun getItemCount(): Int = listItem?.size!!

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Favorite>() {
            override fun areItemsTheSame(
                oldItem: Favorite,
                newItem: Favorite
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Favorite,
                newItem: Favorite
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}