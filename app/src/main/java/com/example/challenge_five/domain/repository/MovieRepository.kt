package com.example.challenge_five.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.challenge_five.common.Constants
import com.example.challenge_five.common.Resource
import com.example.challenge_five.data.local.entity.UserEntity
import com.example.challenge_five.data.local.entity.toDomain
import com.example.challenge_five.data.local.room.dao.MovieDao
import com.example.challenge_five.data.local.room.dao.UserDao
import com.example.challenge_five.data.remote.ApiService
import com.example.challenge_five.domain.model.Detail
import com.example.challenge_five.domain.model.Favorite
import com.example.challenge_five.domain.model.Result
import com.example.challenge_five.domain.model.toEntity
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val movieDao: MovieDao
) {
    fun getMovieList(): LiveData<Resource<List<Result>>> = liveData {
        try {
            emit(Resource.Loading())
            val response = apiService.getMovies(Constants.API_KEY)
            val movies = response.results
            emit(Resource.Success(movies))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Something went wrong!"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your connection"))
        }
    }

    fun getUser(email: String): LiveData<UserEntity> = liveData {
        val result: LiveData<UserEntity> = userDao.getUser(email)
        emitSource(result)
    }

    fun getMovieDetail(movieId: Int): LiveData<Resource<Detail>> = liveData {
        try {
            emit(Resource.Loading())
            val response = apiService.getMovieDetails(movieId, Constants.API_KEY)
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Something went wrong!"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your connection"))
        }
    }

    suspend fun getFavorites(id: Int): Resource<List<Favorite>> {
        return try {
            val data = movieDao.getFavorite(id).map { it.toDomain() }
            Resource.Success(data)
        } catch (e: HttpException) {
            Resource.Error(e.localizedMessage ?: "Something went wrong!")
        } catch (e: IOException) {
            Resource.Error("Couldn't reach server. Check your connection")
        }
    }


    suspend fun getFavouritesMovies(userId: Int, movieId: Int?): Favorite? {
        return movieDao.getFavoriteMovie(userId, movieId)?.toDomain()
    }

    suspend fun removeFromFavorite(userId: Int, movieId: Int?) {
        movieDao.removeFavoriteMovie(userId, movieId)
    }

    suspend fun addToFavorite(movie: Result) {
        movieDao.insertFavoriteMovie(movie.toEntity())
    }

    fun login(email: String, password: String): UserEntity {
        return userDao.login(email, password)
    }

    suspend fun register(user: UserEntity): Long =
        userDao.register(user)

    suspend fun update(user: UserEntity): Int =
        userDao.update(user)

    companion object {
        @Volatile
        private var instance: MovieRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            movieDao: MovieDao
        ): MovieRepository =
            instance ?: synchronized(this) {
                instance ?: MovieRepository(apiService, userDao, movieDao)
            }.also { instance = it }
    }
}