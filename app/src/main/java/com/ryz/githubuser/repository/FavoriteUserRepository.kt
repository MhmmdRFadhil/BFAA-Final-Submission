package com.ryz.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ryz.githubuser.database.FavoriteDao
import com.ryz.githubuser.model.UserFavorite
import com.ryz.githubuser.database.FavoriteUserRoomDatabase

class FavoriteUserRepository(application: Application) {
    private val favoriteDao: FavoriteDao

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        favoriteDao = db.favoriteDao()
    }

    fun getAllFavoriteUser(): LiveData<List<UserFavorite>> = favoriteDao.getAllFavoriteUser()

    fun insert(userFavorite: UserFavorite) = favoriteDao.insert(userFavorite)

    fun check(id: Int) = favoriteDao.check(id)

    fun delete(id: Int) = favoriteDao.delete(id)
}