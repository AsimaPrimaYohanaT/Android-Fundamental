package com.example.submissiongithubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert
    fun addFavorite(favorite: Favorite)

    @Update
    fun update(favorite: Favorite)

    @Query("SELECT * from favorite ORDER BY id ASC")
    fun getFavorite(): LiveData<List<Favorite>>

    @Query("SELECT count(*) FROM favorite WHERE favorite.id = :id")
    fun checkUser(id: Int): Int

    @Query("DELETE FROM favorite WHERE favorite.id = :id")
    fun removeFavorite(id: Int): Int

}