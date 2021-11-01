package com.ryz.githubuser.model

import com.google.gson.annotations.SerializedName

data class UserSearch(

	@field:SerializedName("items")
	val items: List<UserSearchResponse>
)
