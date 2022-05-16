package com.example.challenge_five.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.challenge_five.data.local.entity.FavoriteEntity
import com.example.challenge_five.domain.model.Favorite

@Dao
interface MovieDao {
    @Query("SELECT * FROM favorite_movies WHERE user_id = :userId")
    suspend fun getFavorite(userId: Int): List<FavoriteEntity>

    @Query("SELECT * FROM favorite_movies WHERE user_id = :userId AND movie_id = :movieId")
    suspend fun getFavoriteMovie(userId: Int, movieId: Int?): FavoriteEntity?

    @Query("DELETE FROM favorite_movies WHERE user_id = :userId AND movie_id = :movieId")
    suspend fun removeFavoriteMovie(userId: Int, movieId: Int?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(movie: FavoriteEntity)
}