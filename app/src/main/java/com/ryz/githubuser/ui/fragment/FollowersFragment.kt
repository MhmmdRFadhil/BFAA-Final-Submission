package com.ryz.githubuser.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ryz.githubuser.R
import com.ryz.githubuser.adapter.UserFollowersAdapter
import com.ryz.githubuser.databinding.FragmentFollowersBinding
import com.ryz.githubuser.interfaces.ItemClickCallback
import com.ryz.githubuser.model.UserData
import com.ryz.githubuser.ui.DetailUserActivity
import com.ryz.githubuser.utils.show
import com.ryz.githubuser.viewmodel.FollowersViewModel

class FollowersFragment : Fragment() {

    private var binding: FragmentFollowersBinding? = null
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var userFollowersAdapter: UserFollowersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followersViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowersViewModel::class.java]

        arguments?.let {
            followersViewModel.setListUserFollowers(
                requireArguments().getString(DetailUserActivity.EXTRA_DATA_FRAGMENT).toString(),
                requireContext()
            )
        }

        followersViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        getDataUserFollowers()
    }

    private fun getDataUserFollowers() {
        binding?.apply {
            rvFollowers.setHasFixedSize(true)
            rvFollowers.layoutManager = LinearLayoutManager(context)
            userFollowersAdapter = UserFollowersAdapter()
            rvFollowers.adapter = userFollowersAdapter

            followersViewModel.getListUserFollowers().observe(viewLifecycleOwner) { listUser ->
                listUser?.let {
                    if (it.size == 0) {
                        emptyData()
                    } else {
                        userFollowersAdapter.setData(it)
                    }
                }
            }
        }

        userFollowersAdapter.setOnItemClickCallback(object : ItemClickCallback {
            override fun onItemClicked(data: UserData) {
                showClickedItemUser(data)
            }
        })
    }

    private fun showClickedItemUser(data: UserData) {
        val intent = Intent(context, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_DATA_USER, data)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.apply {
            progressBarDetailFollowers.visibility = if (isLoading) View.VISIBLE else View.GONE
            tvLoadingDataFollowers.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun emptyData() {
        binding?.apply {
            layoutEmptyData.let {
                it.tvTitleEmptyData.text = getString(R.string.title_no_followers)
                it.tvSubTitleEmptyData.text = getString(R.string.subtitle_no_followers)
                it.root.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}