package com.example.challenge_five.domain.model

import com.example.challenge_five.data.local.entity.FavoriteEntity
import com.google.gson.annotations.SerializedName

data class Favorite(
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("movie_id")
    val movieId: Int? = 0,
    @SerializedName("user_id")
    val userId: Int? = 0,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
)
fun Favorite.toDomain(): FavoriteEntity =
    FavoriteEntity(
        id = id,
        userId = userId,
        movieId = id,
        title = title,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        overview = overview,
        popularity = popularity,
        voteAverage = voteAverage,
    )