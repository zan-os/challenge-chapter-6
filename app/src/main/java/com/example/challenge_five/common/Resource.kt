package com.example.challenge_five.common

sealed class Resource<T>(val status: Status, val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : Resource<T>(Status.SUCCESS, data)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(Status.ERROR, data, message)
    class Loading<T>(data: T? = null) : Resource<T>(Status.LOADING, data)
}