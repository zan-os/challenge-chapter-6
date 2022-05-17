package com.example.challenge_five.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge_five.data.local.entity.UserEntity
import com.example.challenge_five.domain.repository.MovieRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel(
    private val getMovieRepository: MovieRepository
) : ViewModel() {
    private var update = 0

    fun update(userEntity: UserEntity): Int {
        viewModelScope.launch {
            update = getMovieRepository.update(userEntity)
        }
        return update
    }
}