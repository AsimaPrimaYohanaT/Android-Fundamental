package com.example.submissiongithubuser.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.submissiongithubuser.*
import com.example.submissiongithubuser.databinding.ActivityDetailBinding
import com.example.submissiongithubuser.ui.main.ListUserAdapter
import com.example.submissiongithubuser.ui.response.DetailResponse
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var adapter: ListUserAdapter
    companion object {
        const val DETAIL_USER = "detail_user"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(DETAIL_USER)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)
        val bundle = Bundle()
        bundle.putString(DETAIL_USER, username)

        adapter = ListUserAdapter()

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        username?.let { viewModel.getUser(it) }
        showTabLayout()

        viewModel.getUserDetail().observe(this) {
            binding.apply {
                tvUsername.text = it.login
                tvName.text = it.name
                tvFollowers.text = ("Followers " + it.followers.toString())
                tvFollowing.text = ("Following " + it.following.toString())
                Glide.with(this@DetailActivity)
                    .load(it.avatarUrl)
                    .circleCrop()
                    .into(ivPicture)
            }
        }

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleFavorite.isChecked = true
                        _isChecked = true
                    } else {
                        binding.toggleFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.toggleFavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                if (username != null) {
                    if (avatarUrl != null) {
                        viewModel.addFavorite(username, id, avatarUrl)
                    }
                    Toast.makeText(
                        this,
                        "$username  has been successfully added to favorites",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                viewModel.removeFavorite(id)
                Toast.makeText(this, "$username has been removed from favorite", Toast.LENGTH_LONG)
                    .show()
            }
            binding.toggleFavorite.isChecked = _isChecked
        }

        val detailUser = intent.getStringExtra(DETAIL_USER)
        binding.tvName.text = detailUser

        if (detailUser != null) {
            showLoading(true)
            viewModel.getUser(detailUser)
            showLoading(false)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        showLoading(true)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)
        viewModel.getUserDetail().observe(this, {
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

    private fun showTabLayout() {
        val username = intent.getStringExtra(DETAIL_USER)
        val bundle = Bundle()
        bundle.putString(DETAIL_USER, username)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }
}