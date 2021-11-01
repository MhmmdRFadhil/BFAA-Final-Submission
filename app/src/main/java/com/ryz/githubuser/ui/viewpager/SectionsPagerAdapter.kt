package com.ryz.githubuser.ui.viewpager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ryz.githubuser.ui.fragment.FollowersFragment
import com.ryz.githubuser.ui.fragment.FollowingFragment

class SectionsPagerAdapter(
    activity: AppCompatActivity,
    private val tabTitle: IntArray,
    private val username: Bundle
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = tabTitle.size

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        fragment?.arguments = username
        return fragment!!
    }
}