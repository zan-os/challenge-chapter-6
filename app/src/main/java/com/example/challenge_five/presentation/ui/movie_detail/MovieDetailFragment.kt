package com.example.challenge_five.presentation.ui.movie_detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.challenge_five.R
import com.example.challenge_five.common.Constants
import com.example.challenge_five.common.Resource
import com.example.challenge_five.databinding.FragmentMovieDetailBinding
import com.example.challenge_five.domain.model.Detail
import com.example.challenge_five.domain.model.Result
import com.example.challenge_five.presentation.ui.ViewModelFactory

class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val args: MovieDetailFragmentArgs by navArgs()

    private lateinit var factory: ViewModelFactory
    private val viewModel: MovieDetailViewModel by viewModels {
        factory
    }

    private val userId: Int by lazy { args.userId }
    private val movie: Result by lazy { args.movie }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        factory = ViewModelFactory.getInstance(requireActivity())
        _binding = FragmentMovieDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply {
            getFavoritesMovie(userId, movie.id)
        }
        Log.d("movie", movie.toString())

        getMovieDetails()
        popBackStack()
        favouritesMovieInit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getMovieDetails() {
        val movieId = args.id
        viewModel.getMovieDetails(movieId).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Resource.Loading -> {
                        setLoading(true)
                    }
                    is Resource.Success -> {
                        setLoading(false)
                        val detail = result.data
                        showMovieDetails(detail)
                    }
                    is Resource.Error -> {
                        setLoading(false)
                    }
                }
            }
        }
    }

    private fun favouritesMovieInit() {
        viewModel.favoriteMovie.observe(viewLifecycleOwner) { result ->
            val isFavorite = result?.movieId == movie.id
            binding.toggleFavorite.setOnClickListener {
                if (result == null) {
                    movie.userId = userId
                    viewModel.addToFavorite(movie)
                showToast("Berhasil menambahkan ke Favorite")
                } else {
                    viewModel.removeFromFavorite(userId, result.movieId)
                    showToast("Berhasil menghapus Favorite")
                }
            }
            binding.toggleFavorite.isChecked = isFavorite
        }
    }

    private fun showMovieDetails(movie: Detail?) {
        binding.apply {
            val ratingText = getString(R.string.get_rating, movie?.voteAverage.toString())
            val durationText = getString(R.string.get_duration, movie?.runtime.toString())
            titleTextView.text = movie?.title
            ratingTextView.text = ratingText
            movieDurationTextView.text = durationText
            movieLanguageTextView.text = movie?.originalLanguage
            movieReleaseDateTextView.text = movie?.releaseDate
            overviewTextView.text = movie?.overview
            Glide.with(this@MovieDetailFragment)
                .load(Constants.BASE_IMAGE_URL + movie?.posterPath)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                )
                .into(binding.posterImageView)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun popBackStack() {
        binding.toolbarId.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}