package com.example.challenge_five.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.challenge_five.domain.model.Favorite

@Entity(tableName = "favorite_movies")
data class FavoriteEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = 0,

    @ColumnInfo(name = "movie_id")
    val movieId: Int? = 0,

    @ColumnInfo(name = "user_id")
    val userId: Int? = 0,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "poster_path")
    val posterPath: String?,

    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String?,

    @ColumnInfo(name = "release_date")
    val releaseDate: String?,

    @ColumnInfo(name = "overview")
    val overview: String?,

    @ColumnInfo(name = "popularity")
    val popularity: Double? = 0.0,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double? = 0.0,
)

fun FavoriteEntity.toDomain(): Favorite =
    Favorite(
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
