package com.example.challenge_five.presentation.ui.movie_list

import androidx.lifecycle.ViewModel
import com.example.challenge_five.domain.repository.MovieRepository
import javax.inject.Inject

class MovieListViewModel(
    private val getMovieRepository: MovieRepository
) : ViewModel() {
    fun getMovieList() = getMovieRepository.getMovieList()
    fun getUser(email: String) = getMovieRepository.getUser(email)
}