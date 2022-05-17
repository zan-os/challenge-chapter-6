package com.example.challenge_five.presentation.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge_five.data.local.entity.UserEntity
import com.example.challenge_five.domain.repository.MovieRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel(
    private val repository: MovieRepository
) : ViewModel() {
    private val register = MutableLiveData<Long>()

    fun register(username: String, email: String, password: String): LiveData<Long> {
        val user = UserEntity(
            username = username,
            email = email,
            password = password
        )

        viewModelScope.launch {
            register.value = repository.register(user)
        }
        return register
    }
}