package com.ryz.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.ryz.githubuser.R
import com.ryz.githubuser.databinding.ActivityDetailUserBinding
import com.ryz.githubuser.model.UserData
import com.ryz.githubuser.settings.SettingViewModelFactory
import com.ryz.githubuser.ui.viewpager.SectionsPagerAdapter
import com.ryz.githubuser.utils.loadImageUrl
import com.ryz.githubuser.viewmodel.DetailUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailUserViewModel = obtainViewModel(this)

        userData =
            intent.extras?.getParcelable<UserData>(EXTRA_DATA_USER) as UserData

        userData.username.let {
            val username = Bundle()
            username.putString(EXTRA_DATA_FRAGMENT, userData.username)
            detailUserViewModel.setDetailUser(it.toString())

            // action bar
            setActionBarTitle(userData.username.toString())

            // tab layout with view pager
            val sectionPagerAdapter = SectionsPagerAdapter(this, TAB_TITLES, username)
            binding.viewPager.apply {
                adapter = sectionPagerAdapter
                (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            }
            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()

        }

        getData(userData)
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = SettingViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
    }

    private fun getData(userData: UserData) {
        binding.apply {
            detailUserViewModel.getUserSearch().observe(this@DetailUserActivity) { listUser ->
                listUser?.let { user ->
                    user.forEach { detailUser ->
                        imgDetailUser.loadImageUrl(detailUser.image.toString())
                        tvDetailName.text = detailUser.name ?: getString(R.string.not_known)
                        tvDetailUsername.text = detailUser.username
                        tvCountFollowers.text = detailUser.followers
                        tvCountFollowing.text = detailUser.following
                        tvDetailLocation.text = detailUser.location ?: getString(R.string.not_known)
                        tvDetailCompany.text = detailUser.company ?: getString(R.string.not_known)
                        tvCountRepository.text = detailUser.repository

                        var isFavorite = false
                        CoroutineScope(Dispatchers.IO).launch {
                            userData.id?.let {
                                val count = detailUserViewModel.check(it)
                                isFavorite = if (count > 0) {
                                    setStatusFavorite(true)
                                    true
                                } else {
                                    false
                                }
                            }
                        }

                        fabAddUser.setOnClickListener {
                            userData.id?.let { userId ->
                                isFavorite = !isFavorite
                                if (isFavorite) {
                                    detailUserViewModel.insert(
                                        userId,
                                        userData.username.toString(),
                                        userData.avatarUrl.toString(),
                                        userData.userType.toString()
                                    )
                                    toastMessage(getString(R.string.add_successfully))
                                } else {
                                    detailUserViewModel.delete(userId)
                                    toastMessage(getString(R.string.delete_successfully))
                                }
                                setStatusFavorite(isFavorite)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setStatusFavorite(isFavorite: Boolean) {
        binding.apply {
            if (isFavorite) fabAddUser.setImageResource(R.drawable.ic_favorite_selected)
            else fabAddUser.setImageResource(R.drawable.ic_favorite_unselected)
        }
    }

    private fun setActionBarTitle(username: String) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = username
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                val intent = Intent()
                intent.apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Github User \n\nName : ${
                            binding.tvDetailName.text
                        } \nUsername : ${
                            binding.tvDetailUsername.text
                        } \nFollowers : ${
                            binding.tvCountFollowers.text
                        } \nFollowing : ${
                            binding.tvCountFollowing.text
                        } \nLocation : ${
                            binding.tvDetailLocation.text
                        } \nCompany : ${
                            binding.tvDetailCompany.text
                        } \nRepository : ${binding.tvCountRepository.text}",
                    )
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(intent, "Share"))
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        @StringRes
        val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_DATA_USER = "extra_data"
        const val EXTRA_DATA_FRAGMENT = "extra_data_fragment"
    }
}