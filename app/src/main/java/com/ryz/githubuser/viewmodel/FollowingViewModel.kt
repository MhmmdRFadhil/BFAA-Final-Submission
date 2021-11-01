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

class FollowingViewModel : ViewModel() {
    private val listUserFollowingMutable = MutableLiveData<ArrayList<UserData>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setListUserFollowing(username: String, context: Context) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UserSearchResponse>> {
            override fun onResponse(
                call: Call<List<UserSearchResponse>>,
                response: Response<List<UserSearchResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val listUserFollowing = ArrayList<UserData>()
                    listUserFollowing.clear()
                    if (response.body() != null) {
                        response.body()!!.forEach { user ->
                            listUserFollowing.add(
                                UserData(
                                    user.id,
                                    user.login,
                                    user.type,
                                    user.avatarUrl
                                )
                            )
                        }
                    }
                    listUserFollowingMutable.postValue(listUserFollowing)
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

    internal fun getListUserFollowing(): LiveData<ArrayList<UserData>> {
        return listUserFollowingMutable
    }

    companion object {
        private const val TAG = "FollowingViewModel"
    }
}