package com.example.challenge_five.presentation.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.challenge_five.di.Injection
import com.example.challenge_five.domain.repository.MovieRepository
import com.example.challenge_five.presentation.ui.favorite_list.FavoriteFragment
import com.example.challenge_five.presentation.ui.favorite_list.FavoriteViewModel
import com.example.challenge_five.presentation.ui.login.LoginViewModel
import com.example.challenge_five.presentation.ui.movie_detail.MovieDetailViewModel
import com.example.challenge_five.presentation.ui.movie_list.MovieListViewModel
import com.example.challenge_five.presentation.ui.profile.ProfileViewModel
import com.example.challenge_five.presentation.ui.register.RegisterViewModel

class ViewModelFactory private constructor(private val movieRepository: MovieRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieListViewModel::class.java)) {
            return MovieListViewModel(movieRepository) as T
        } else if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            return MovieDetailViewModel(movieRepository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(movieRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(movieRepository) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(movieRepository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class : " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}