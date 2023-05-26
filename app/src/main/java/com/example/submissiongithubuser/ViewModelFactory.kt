package com.example.submissiongithubuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissiongithubuser.ui.theme.SettingPreferences
import com.example.submissiongithubuser.ui.theme.ThemeSettingViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeSettingViewModel::class.java)) {
            return ThemeSettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class" + modelClass.name)
    }
}