package com.ryz.githubuser.model

import com.google.gson.annotations.SerializedName

data class UserSearchResponse(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("id")
	val id: Int,

)
