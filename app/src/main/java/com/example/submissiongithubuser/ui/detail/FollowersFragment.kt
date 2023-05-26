package com.example.submissiongithubuser.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissiongithubuser.R
import com.example.submissiongithubuser.databinding.FragmentFollowBinding
import com.example.submissiongithubuser.ui.main.ListUserAdapter

class FollowersFragment : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowerViewModel
    private lateinit var adapter: ListUserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailActivity.DETAIL_USER).toString()

        _binding = FragmentFollowBinding.bind(view)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvFollow.setHasFixedSize(true)
            rvFollow.layoutManager = LinearLayoutManager(activity)
            rvFollow.adapter = adapter

        }
        showLoading(true)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowerViewModel::class.java)
        viewModel.listFollowersSet(username)
        viewModel.getFollower().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setListData(it)
                showLoading(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
