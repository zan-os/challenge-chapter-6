package com.example.challenge_five.presentation.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge_five.common.Resource
import com.example.challenge_five.data.local.entity.UserEntity
import com.example.challenge_five.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel(
    private val repository: MovieRepository
) : ViewModel() {
    private val _result = MutableLiveData<Resource<UserEntity>>()
    val result: LiveData<Resource<UserEntity>> = _result

    fun login(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _result.postValue(Resource.Loading())
            try {
                Log.d("Thread mana", Thread.currentThread().name)
                val data = repository.login(email, password)
                _result.postValue(Resource.Success(data))
            } catch (e: Exception) {
                Log.d("Thread mana", Thread.currentThread().name)
                _result.postValue(Resource.Error(e.message))
            }
        }
    }

}