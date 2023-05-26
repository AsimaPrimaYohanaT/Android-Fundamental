package com.example.submissiongithubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submissiongithubuser.ui.response.DetailResponse
import com.example.submissiongithubuser.database.Favorite
import com.example.submissiongithubuser.database.FavoriteRepository
import com.example.submissiongithubuser.ui.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "DetailViewModel"
        private var USERNAME = ""
    }
    val getAllData: LiveData<List<Favorite>>

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    init {
        getAllData = mFavoriteRepository.getFavorite()
    }
    private val _detailUser = MutableLiveData<DetailResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addFavorite(username: String, id: Int, avatarUrl: String) {
        mFavoriteRepository.addFavorite(username, id, avatarUrl)
    }

    init{
        getUser(USERNAME)
    }

    fun removeFavorite(id: Int) {
        mFavoriteRepository.removeFavorite(id)
    }

    suspend fun checkUser(id: Int) = mFavoriteRepository.checkUser(id)

    fun getUser(username : String) {
        _isLoading.value= true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>,
                                    response: Response<DetailResponse>)
            {
                _isLoading.value=false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value=false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUserDetail(): LiveData<DetailResponse> {
        return _detailUser
    }
}