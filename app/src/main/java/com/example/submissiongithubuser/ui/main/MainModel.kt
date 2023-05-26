package com.example.submissiongithubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissiongithubuser.User
import com.example.submissiongithubuser.ui.response.UserResponse
import com.example.submissiongithubuser.ui.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainModel:ViewModel() {

    companion object {
        private const val TAG = "MainModel"
        private var USERNAME = "A"
    }

    private val _listUsers = MutableLiveData<List<User>>()
    val listUsers: LiveData<List<User>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<List<User>> {
        return listUsers
    }

    init {
        findUser(USERNAME)
    }


    fun findUser(username : String) {
        _isLoading.value= true
        val client = ApiConfig.getApiService().getListUser(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value= false
                if (response.isSuccessful) {
                    _listUsers.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value= false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


}