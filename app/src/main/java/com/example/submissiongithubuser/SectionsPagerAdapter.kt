package com.example.submissiongithubuser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.submissiongithubuser.ui.detail.FollowersFragment
import com.example.submissiongithubuser.ui.detail.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, data: Bundle) : FragmentStateAdapter(activity) {
    var username: String = ""
    private var fragmentBundle: Bundle

    init {
        fragmentBundle = data
    }
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }
    override fun getItemCount(): Int {
        return 2
    }
}