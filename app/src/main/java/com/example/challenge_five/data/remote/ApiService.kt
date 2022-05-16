package com.example.challenge_five.data.remote

import com.example.challenge_five.domain.model.Detail
import com.example.challenge_five.domain.model.Movie
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("api_key") apiKey: String
    ): Movie

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Detail
}