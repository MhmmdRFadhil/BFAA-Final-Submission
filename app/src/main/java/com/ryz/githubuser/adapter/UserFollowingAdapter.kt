package com.ryz.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ryz.githubuser.databinding.ItemRowUserFollowingBinding
import com.ryz.githubuser.interfaces.ItemClickCallback
import com.ryz.githubuser.model.UserData
import com.ryz.githubuser.utils.MyDiffUtils
import com.ryz.githubuser.utils.loadImageUrl

class UserFollowingAdapter : RecyclerView.Adapter<UserFollowingAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: ItemClickCallback
    private val userFollowing = ArrayList<UserData>()

    fun setOnItemClickCallback(onItemClickCallback: ItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<UserData>) {
        val diffUtils = MyDiffUtils(userFollowing, items)
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        userFollowing.clear()
        userFollowing.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private var binding: ItemRowUserFollowingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userData: UserData) {
            binding.apply {
                imgFollowing.loadImageUrl(userData.avatarUrl.toString())
                tvNameFollowing.text = userData.userType
                tvUsernameFollowing.text = userData.username

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(userData)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowUserFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userFollowing[position])
    }

    override fun getItemCount(): Int = userFollowing.size
}