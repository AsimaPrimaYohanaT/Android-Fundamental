package com.example.submissiongithubuser.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissiongithubuser.ui.main.ListUserAdapter
import com.example.submissiongithubuser.User
import com.example.submissiongithubuser.database.Favorite
import com.example.submissiongithubuser.databinding.ActivityFavoriteBinding
import com.example.submissiongithubuser.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: ListUserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite Users"

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@FavoriteActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.DETAIL_USER, data.login)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }

        })

        binding.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.adapter = adapter
            rvUsers.layoutManager = LinearLayoutManager(this@FavoriteActivity)
        }

        viewModel.getAllData.observe(this, {
            if (it != null) {
                val list = mapList(it)
                adapter.setListData(list)
            }
        })

        showLoading(true)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FavoriteViewModel::class.java)
        viewModel.getAllData.observe(this, {
            if (it != null) {
                showLoading(false)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun mapList(users: List<Favorite>): ArrayList<User> {
        val listUsers = ArrayList<User>()
        for (user in users) {
            val userMapped = User(
                user.login,
                user.avatar_url,
                user.id
            )
            listUsers.add(userMapped)
        }
        return listUsers

    }
}