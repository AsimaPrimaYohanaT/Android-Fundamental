package com.example.submissiongithubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.submissiongithubuser.database.Favorite
import com.example.submissiongithubuser.database.FavoriteRepository

class FavoriteViewModel (application: Application): AndroidViewModel(application){

    val getAllData: LiveData<List<Favorite>>

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    init {
        getAllData = mFavoriteRepository.getFavorite()
    }

}