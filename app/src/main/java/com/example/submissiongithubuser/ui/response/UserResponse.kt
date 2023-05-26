package com.example.submissiongithubuser.ui.response

import com.example.submissiongithubuser.User
import com.google.gson.annotations.SerializedName

data class UserResponse(
	@field:SerializedName("items")
	val items: List<User>
)
