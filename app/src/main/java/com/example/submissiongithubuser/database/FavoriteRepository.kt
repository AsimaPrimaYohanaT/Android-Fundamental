package com.example.submissiongithubuser.database

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {

    private var mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val Database = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = Database.favoriteDao()
    }

    fun getFavorite(): LiveData<List<Favorite>> = mFavoriteDao.getFavorite()

    fun addFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = Favorite(
                username,
                id,
                avatarUrl
            )
            mFavoriteDao.addFavorite(user)
        }
    }

    fun update(favorite: Favorite) {
        executorService.execute { mFavoriteDao.update(favorite) }
    }

    fun removeFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            mFavoriteDao.removeFavorite(id)
        }
    }


    fun checkUser(id: Int) = mFavoriteDao.checkUser(id)
}