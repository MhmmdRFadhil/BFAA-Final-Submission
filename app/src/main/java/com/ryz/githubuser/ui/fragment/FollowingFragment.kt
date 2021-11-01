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
import com.ryz.githubuser.adapter.UserFollowingAdapter
import com.ryz.githubuser.databinding.FragmentFollowingBinding
import com.ryz.githubuser.interfaces.ItemClickCallback
import com.ryz.githubuser.model.UserData
import com.ryz.githubuser.ui.DetailUserActivity
import com.ryz.githubuser.utils.show
import com.ryz.githubuser.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private var binding: FragmentFollowingBinding? = null
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var userFollowingAdapter: UserFollowingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowingViewModel::class.java]


        arguments?.let {
            followingViewModel.setListUserFollowing(
                requireArguments().getString(DetailUserActivity.EXTRA_DATA_FRAGMENT).toString(),
                requireContext()
            )
        }

        followingViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        getDataUserFollowing()
    }

    private fun getDataUserFollowing() {
        binding?.apply {
            rvFollowing.setHasFixedSize(true)
            rvFollowing.layoutManager = LinearLayoutManager(context)
            userFollowingAdapter = UserFollowingAdapter()
            rvFollowing.adapter = userFollowingAdapter

            followingViewModel.getListUserFollowing().observe(viewLifecycleOwner) { listUser ->
                listUser?.let {
                    if (it.size == 0) {
                        emptyData()
                    } else {
                        userFollowingAdapter.setData(it)
                    }
                }
            }
        }
        userFollowingAdapter.setOnItemClickCallback(object : ItemClickCallback {
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
            progressBarDetailFollowing.visibility = if (isLoading) View.VISIBLE else View.GONE
            tvLoadingDataFollowing.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun emptyData() {
        binding?.apply {
            layoutEmptyData.let {
                it.tvTitleEmptyData.text = getString(R.string.title_no_following)
                it.tvSubTitleEmptyData.text = getString(R.string.subtitle_no_following)
                it.root.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}