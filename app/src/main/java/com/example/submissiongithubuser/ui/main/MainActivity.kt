package com.example.submissiongithubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.datastore.core.DataStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissiongithubuser.R
import com.example.submissiongithubuser.User
import com.example.submissiongithubuser.ViewModelFactory
import com.example.submissiongithubuser.databinding.ActivityMainBinding
import com.example.submissiongithubuser.ui.detail.DetailActivity
import com.example.submissiongithubuser.ui.favorite.FavoriteActivity
import com.example.submissiongithubuser.ui.theme.SettingPreferences
import com.example.submissiongithubuser.ui.theme.ThemeSettingActivity
import com.example.submissiongithubuser.ui.theme.ThemeSettingViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainModel: MainModel
    private lateinit var themeSettingViewModel: ThemeSettingViewModel
    private lateinit var listUserAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainModel::class.java)

        val pref = SettingPreferences.getInstance(dataStore)
        themeSettingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            ThemeSettingViewModel::class.java
        )

        listUserAdapter = ListUserAdapter()
        listUserAdapter.notifyDataSetChanged()
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.DETAIL_USER, data.login)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = listUserAdapter

        }
        mainModel.getUser().observe(this) {
            if (it != null) {
                listUserAdapter.setListData(it)
                showLoading(false)
            }
        }
        mainModel.isLoading.observe(this) {
            showLoading(it)
        }


        themeSettingViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)



        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainModel.findUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mainModel.findUser(newText)
                return false
            }
        })
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu2 -> {
                val settingIntent = Intent(this, ThemeSettingActivity::class.java)
                startActivity(settingIntent)
                true
            }
            R.id.favorite -> {
                val favIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(favIntent)
                true
            }
            else -> false

        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}