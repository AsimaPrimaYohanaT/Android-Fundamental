package com.example.submissiongithubuser.ui.api

import com.example.submissiongithubuser.ui.response.DetailResponse
import com.example.submissiongithubuser.User
import com.example.submissiongithubuser.ui.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getListUser(
        @Query("q") login: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>



}