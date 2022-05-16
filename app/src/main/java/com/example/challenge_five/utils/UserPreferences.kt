package com.example.challenge_five.utils

import android.content.Context
import com.example.challenge_five.data.local.entity.UserEntity

class UserPreferences(
    private val context: Context
) {
    private val preferences = "auth"
    private val sharedPreferences = context.getSharedPreferences(preferences, Context.MODE_PRIVATE)

    private val edit = sharedPreferences.edit()

    fun getUser(user: UserEntity) {
        edit.putInt("id", user.id)
        edit.putString("username", user.username)
        edit.putString("name", user.name)
        edit.putString("email", user.email)
        edit.putString("password", user.password)
        edit.putString("address", user.address)
        edit.putString("date_of_birth", user.dateOfBirth)
        edit.apply()
    }

    fun saveLoginState(status: Boolean) {
        edit.putBoolean("login_state", status)
        edit.apply()
    }

    fun isLogin(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getKey(key: String): String {
        return sharedPreferences.getString(key, "null").toString()
    }

    fun getKeyId(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun logout() {
        edit.apply()
        edit.clear()
    }
}