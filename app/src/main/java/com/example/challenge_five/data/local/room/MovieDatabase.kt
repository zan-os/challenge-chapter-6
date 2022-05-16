package com.example.challenge_five.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.challenge_five.data.local.entity.FavoriteEntity
import com.example.challenge_five.data.local.entity.UserEntity
import com.example.challenge_five.data.local.room.dao.MovieDao
import com.example.challenge_five.data.local.room.dao.UserDao

@Database(entities = [UserEntity::class, FavoriteEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var instance: MovieDatabase? = null
        fun getInstance(context: Context): MovieDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java, "Movie.db"
                ).build()
            }
    }
}