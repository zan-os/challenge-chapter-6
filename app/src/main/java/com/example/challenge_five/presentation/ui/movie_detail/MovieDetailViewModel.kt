package com.example.challenge_five.presentation.ui.movie_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge_five.domain.model.Favorite
import com.example.challenge_five.domain.model.Result
import com.example.challenge_five.domain.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    private val _favoriteMovie = MutableLiveData<Favorite?>()
    val favoriteMovie: LiveData<Favorite?> = _favoriteMovie

    fun getMovieDetails(movieId: Int) = repository.getMovieDetail(movieId)

    fun getFavoritesMovie(userId: Int, movieId: Int?) {
        viewModelScope.launch {
            val data = async { repository.getFavouritesMovies(userId, movieId) }
            val result = data.await()
            _favoriteMovie.postValue(result)
        }
    }

    fun addToFavorite(movie: Result) {
        viewModelScope.launch {
            repository.addToFavorite(movie)
        }
    }

    fun removeFromFavorite(userId: Int, movieId: Int?) {
        viewModelScope.launch {
            repository.removeFromFavorite(userId, movieId)
        }
    }
}