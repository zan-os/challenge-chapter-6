package com.example.challenge_five.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.challenge_five.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun register(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUser(email: String): LiveData<UserEntity>

    @Query("SELECT * FROM users WHERE email LIKE :email AND password LIKE :password")
    fun login(email: String, password: String): UserEntity

    @Update
    suspend fun update(user: UserEntity): Int
}