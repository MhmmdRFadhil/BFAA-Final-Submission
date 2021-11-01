package com.ryz.githubuser.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ryz.githubuser.viewmodel.DetailUserViewModel
import com.ryz.githubuser.viewmodel.FavoriteUserViewModel
import com.ryz.githubuser.viewmodel.MainViewModel
import com.ryz.githubuser.viewmodel.SettingViewModel
import java.lang.IllegalArgumentException

class SettingViewModelFactory constructor(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailUserViewModel::class.java) -> {
                DetailUserViewModel(application) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(application) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(application) as T
            }
            modelClass.isAssignableFrom(FavoriteUserViewModel::class.java) -> {
                FavoriteUserViewModel(application) as T
            }
            else -> throw  IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): SettingViewModelFactory {
            if (INSTANCE == null) {
                synchronized(SettingViewModelFactory::class.java) {
                    INSTANCE = SettingViewModelFactory(application)
                }
            }
            return INSTANCE as SettingViewModelFactory
        }
    }
}