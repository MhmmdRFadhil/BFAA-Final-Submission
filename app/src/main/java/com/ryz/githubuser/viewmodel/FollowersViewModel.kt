package com.ryz.githubuser.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ryz.githubuser.model.UserData
import com.ryz.githubuser.model.UserSearchResponse
import com.ryz.githubuser.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    private val listUserFollowersMutable = MutableLiveData<ArrayList<UserData>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    internal fun setListUserFollowers(username: String, context: Context) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<UserSearchResponse>> {
            override fun onResponse(
                call: Call<List<UserSearchResponse>>,
                response: Response<List<UserSearchResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val listUserFollowers = ArrayList<UserData>()
                    listUserFollowers.clear()
                    if (response.body() != null) {
                        response.body()!!.forEach { user ->
                            listUserFollowers.add(
                                UserData(
                                    user.id,
                                    user.login,
                                    user.type,
                                    user.avatarUrl
                                )
                            )
                        }
                    }
                    listUserFollowersMutable.postValue(listUserFollowers)
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserSearchResponse>>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    internal fun getListUserFollowers(): LiveData<ArrayList<UserData>> = listUserFollowersMutable

    companion object {
        private const val TAG = "FollowersViewModel"
    }
}