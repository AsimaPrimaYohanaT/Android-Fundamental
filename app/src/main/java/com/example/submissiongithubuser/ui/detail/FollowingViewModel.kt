package com.example.submissiongithubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissiongithubuser.User
import com.example.submissiongithubuser.ui.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    companion object {
        val TAG = "FollowingViewModel"
    }

    val listFollowing = MutableLiveData<ArrayList<User>>()

    fun listFollowingSet(username: String) {
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    listFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }


            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.e(TAG, "onFailure ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(): LiveData<ArrayList<User>> {
        return listFollowing
    }
}