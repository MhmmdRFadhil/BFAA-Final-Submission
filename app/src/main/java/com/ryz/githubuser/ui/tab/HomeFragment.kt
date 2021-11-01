package com.ryz.githubuser.ui.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ryz.githubuser.R
import com.ryz.githubuser.adapter.UserAdapter
import com.ryz.githubuser.databinding.FragmentHomeBinding
import com.ryz.githubuser.interfaces.ItemClickCallback
import com.ryz.githubuser.model.UserData
import com.ryz.githubuser.settings.SettingViewModelFactory
import com.ryz.githubuser.ui.DetailUserActivity
import com.ryz.githubuser.utils.hide
import com.ryz.githubuser.utils.show
import com.ryz.githubuser.viewmodel.MainViewModel

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialState()
        searchGithubUser()

        mainViewModel = obtainViewModel(context as AppCompatActivity)

        mainViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        getDataUser()
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = SettingViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]

    }

    private fun getDataUser() {
        binding?.apply {
            rvMain.setHasFixedSize(true)
            rvMain.layoutManager = LinearLayoutManager(context)
            userAdapter = UserAdapter()
            rvMain.adapter = userAdapter

            mainViewModel.getUserSearch().observe(viewLifecycleOwner) { listUser ->
                listUser.let {
                    if (it.size == 0) {
                        emptyState()
                    } else {
                        userAdapter.setData(it)
                        finalState()
                    }
                }
            }
            userAdapter.setOnItemClickCallback(object : ItemClickCallback {
                override fun onItemClicked(data: UserData) {
                    showClickedItemUser(data)
                }
            })
        }
    }

    private fun searchGithubUser() {
        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                userAdapter.userList.clear()
                query.let {
                    binding?.searchView?.clearFocus() // hide keyboard after searching
                    finalState()
                    mainViewModel.setListUser(it.toString())
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun showClickedItemUser(data: UserData) {
        val bundle = Bundle()
        bundle.putParcelable(DetailUserActivity.EXTRA_DATA_USER, data)
        view?.findNavController()?.navigate(R.id.action_homeFragment_to_detailUserActivity, bundle)
    }

    private fun initialState() {
        binding?.apply {
            rvMain.hide()
            layoutEmptyState.root.show()
            layoutEmptyData.root.hide()
        }
    }

    private fun emptyState() {
        binding?.apply {
            rvMain.hide()
            layoutEmptyState.root.hide()
            layoutEmptyData.let {
                it.tvTitleEmptyData.text = getString(R.string.title_result_not_found)
                it.tvSubTitleEmptyData.text = getString(R.string.subtitle_result_not_found)
                it.root.show()
            }
        }
    }

    private fun finalState() {
        binding?.apply {
            rvMain.show()
            layoutEmptyState.root.hide()
            layoutEmptyData.root.hide()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            tvLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}