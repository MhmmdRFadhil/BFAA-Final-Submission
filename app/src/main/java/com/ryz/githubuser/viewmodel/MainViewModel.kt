package com.ryz.githubuser.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.ryz.githubuser.model.UserData
import com.ryz.githubuser.model.UserSearch
import com.ryz.githubuser.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val application: Application): ViewModel() {
    private val listUserMutable = MutableLiveData<ArrayList<UserData>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    internal fun setListUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(query)
        client.enqueue(object : Callback<UserSearch> {
            override fun onResponse(call: Call<UserSearch>, response: Response<UserSearch>) {
                _isLoading.value = false
                val listUser = ArrayList<UserData>()
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        response.body()?.items?.forEach { user ->
                            listUser.add(
                                UserData(
                                    user.id,
                                    user.login,
                                    user.type,
                                    user.avatarUrl
                                )
                            )
                        }
                        listUserMutable.postValue(listUser)
                    }
                } else {
                    Toast.makeText(application.applicationContext, response.message(), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserSearch>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(application.applicationContext, t.message, Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure ${t.message}")
            }
        })
    }

    internal fun getUserSearch(): LiveData<ArrayList<UserData>> = listUserMutable

    companion object {
        private const val TAG = "MainViewModel"
    }
}