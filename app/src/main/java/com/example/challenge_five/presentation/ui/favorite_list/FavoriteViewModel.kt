package com.example.challenge_five.presentation.ui.favorite_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge_five.common.Resource
import com.example.challenge_five.domain.model.Favorite
import com.example.challenge_five.domain.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movies = MutableLiveData<Resource<List<Favorite>>>(Resource.Loading())
    val movies: LiveData<Resource<List<Favorite>>> = _movies

    fun getFavorites(id: Int) {
        viewModelScope.launch {
            val data = async { repository.getFavorites(id) }
            val result = data.await()
            _movies.postValue(result)
        }
    }

    fun removeFavorites(userId: Int, movieId: Int) {
        viewModelScope.launch {
            repository.removeFromFavorite(userId, movieId)
        }
    }
}