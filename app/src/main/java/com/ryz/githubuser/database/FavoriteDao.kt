package com.ryz.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ryz.githubuser.model.UserFavorite

@Dao
interface FavoriteDao {
    @Insert
    fun insert(userFavorite: UserFavorite)

    @Query("SELECT count(*) FROM userFavorite WHERE userFavorite.id = :id")
    fun check(id: Int): Int

    @Query("DELETE FROM userFavorite WHERE userFavorite.id = :id")
    fun delete(id: Int): Int

    @Query("SELECT * FROM userFavorite")
    fun getAllFavoriteUser(): LiveData<List<UserFavorite>>

}