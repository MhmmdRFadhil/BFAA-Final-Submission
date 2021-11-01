package com.ryz.githubuser.networking

import com.ryz.githubuser.model.UserSearchResponse
import com.ryz.githubuser.model.UserDetailResponse
import com.ryz.githubuser.model.UserSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getSearchUser(
        @Query("q") query: String?
    ): Call<UserSearch>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String?
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String?
    ): Call<List<UserSearchResponse>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String?
    ): Call<List<UserSearchResponse>>
}