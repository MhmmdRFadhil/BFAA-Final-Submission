package com.ryz.githubuser.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryz.githubuser.model.UserFavorite
import com.ryz.githubuser.model.UserDataDetail
import com.ryz.githubuser.model.UserDetailResponse
import com.ryz.githubuser.networking.ApiConfig
import com.ryz.githubuser.repository.FavoriteUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(private val application: Application) : ViewModel() {
    private val listDetailUserMutable = MutableLiveData<ArrayList<UserDataDetail>>()
    private val favoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    internal fun setDetailUser(username: String) {
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                val listDetailUser = ArrayList<UserDataDetail>()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val userName: String = responseBody.login
                        val name: String = responseBody.name
                        val image: String = responseBody.avatarUrl
                        val followers: String = responseBody.followers.toString()
                        val following: String = responseBody.following.toString()
                        val company: String = responseBody.company
                        val location: String = responseBody.location
                        val repository: String = responseBody.publicRepos.toString()

                        listDetailUser.add(
                            UserDataDetail(
                                name,
                                userName,
                                image,
                                followers,
                                following,
                                company,
                                location,
                                repository
                            )
                        )
                        listDetailUserMutable.postValue(listDetailUser)
                    }
                } else {
                    Toast.makeText(
                        application.applicationContext,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                Toast.makeText(application.applicationContext, t.message, Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure ${t.message}")
            }
        })
    }

    internal fun getUserSearch(): LiveData<ArrayList<UserDataDetail>> = listDetailUserMutable

    internal fun check(id: Int) = favoriteUserRepository.check(id)

    internal fun insert(id: Int, username: String, avatarUrl: String, userType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = UserFavorite(
                id, username, avatarUrl, userType
            )
            favoriteUserRepository.insert(user)
        }
    }

    internal fun delete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteUserRepository.delete(id)
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}