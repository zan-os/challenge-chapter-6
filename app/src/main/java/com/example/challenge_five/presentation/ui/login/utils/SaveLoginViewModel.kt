package com.example.challenge_five.presentation.ui.login.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

class SaveLoginViewModel(
    private val preferences: UserLoginPreferences
) : ViewModel() {
    val email = preferences.getEmail().asLiveData()

    fun setLogin(email: String) {
        preferences.login(email)
    }
}