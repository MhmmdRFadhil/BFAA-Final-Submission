package com.ryz.githubuser.ui.tab

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ryz.githubuser.R
import com.ryz.githubuser.adapter.UserFavoriteAdapter
import com.ryz.githubuser.model.UserFavorite
import com.ryz.githubuser.databinding.FragmentFavoriteBinding
import com.ryz.githubuser.interfaces.ItemClickCallback
import com.ryz.githubuser.model.UserData
import com.ryz.githubuser.settings.SettingViewModelFactory
import com.ryz.githubuser.ui.DetailUserActivity
import com.ryz.githubuser.utils.hide
import com.ryz.githubuser.utils.show
import com.ryz.githubuser.viewmodel.FavoriteUserViewModel

class FavoriteFragment : Fragment() {
    private var binding: FragmentFavoriteBinding? = null
    private lateinit var favoriteUserViewModel: FavoriteUserViewModel
    private lateinit var userFavoriteAdapter: UserFavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteUserViewModel = obtainViewModel(context as AppCompatActivity)

        getDataUserFavorite()

    }

    private fun getDataUserFavorite() {
        binding?.apply {
            rvFavorite.setHasFixedSize(true)
            rvFavorite.layoutManager = LinearLayoutManager(context)
            userFavoriteAdapter = UserFavoriteAdapter()
            rvFavorite.adapter = userFavoriteAdapter

            favoriteUserViewModel.getAllFavoriteUser().observe(viewLifecycleOwner) { listUser ->
                listUser.let {
                    if (it.isEmpty()) {
                        emptyState()
                    } else {
                        val list = mapListFavorite(listUser)
                        userFavoriteAdapter.setData(list)
                    }
                }
            }
            userFavoriteAdapter.setOnItemClickCallback(object : ItemClickCallback {
                override fun onItemClicked(data: UserData) {
                    showClickedItemFavoriteUser(data)
                }
            })
        }
    }

    private fun mapListFavorite(listUserFavorite: List<UserFavorite>?): ArrayList<UserData> {
        val listUserData = ArrayList<UserData>()
        if (listUserFavorite != null) {
            for (user in listUserFavorite) {
                val userFavorite = UserData(user.id, user.username, user.userType, user.avatarUrl)
                listUserData.add(userFavorite)
            }
        }
        return listUserData
    }

    private fun showClickedItemFavoriteUser(data: UserData) {
        val intent = Intent(context, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_DATA_USER, data)
        startActivity(intent)
    }

    private fun emptyState() {
        binding?.apply {
            layoutEmptyData.let {
                it.tvTitleEmptyData.text = getString(R.string.title_no_favorite)
                it.tvSubTitleEmptyData.text = getString(R.string.subtitle_no_favorite)
                it.root.show()
            }
            rvFavorite.hide()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = SettingViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}