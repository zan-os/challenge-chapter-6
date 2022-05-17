package com.example.challenge_five.di

import android.content.Context
import com.example.challenge_five.data.local.room.MovieDatabase
import com.example.challenge_five.data.remote.ApiConfig
import com.example.challenge_five.domain.repository.MovieRepository

object Injection {
    fun provideRepository(context: Context): MovieRepository {
        val apiService = ApiConfig.getApiService()
        val database = MovieDatabase.getInstance(context)
        val userDao = database.userDao()
        val movieDao = database.movieDao()
        return MovieRepository.getInstance(apiService, userDao, movieDao)
    }
}