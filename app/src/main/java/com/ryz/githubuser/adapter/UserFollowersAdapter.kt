package com.ryz.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ryz.githubuser.databinding.ItemRowUserFollowersBinding
import com.ryz.githubuser.interfaces.ItemClickCallback
import com.ryz.githubuser.model.UserData
import com.ryz.githubuser.utils.MyDiffUtils
import com.ryz.githubuser.utils.loadImageUrl

class UserFollowersAdapter : RecyclerView.Adapter<UserFollowersAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: ItemClickCallback
    private val userFollowers = ArrayList<UserData>()

    fun setOnItemClickCallback(onItemClickCallback: ItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<UserData>) {
        val diffUtils = MyDiffUtils(userFollowers, items)
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        userFollowers.clear()
        userFollowers.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private var binding: ItemRowUserFollowersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userData: UserData) {
            binding.apply {
                imgFollowers.loadImageUrl(userData.avatarUrl.toString())
                tvNameFollowers.text = userData.userType
                tvUsernameFollowers.text = userData.username

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(userData)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowUserFollowersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userFollowers[position])
    }

    override fun getItemCount(): Int = userFollowers.size

}