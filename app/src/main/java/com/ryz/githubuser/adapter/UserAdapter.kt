package com.ryz.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ryz.githubuser.databinding.ItemRowUserBinding
import com.ryz.githubuser.interfaces.ItemClickCallback
import com.ryz.githubuser.model.UserData
import com.ryz.githubuser.utils.MyDiffUtils
import com.ryz.githubuser.utils.loadImageUrl
import kotlin.collections.ArrayList

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: ItemClickCallback
    var userList = ArrayList<UserData>()

    fun setOnItemClickCallback(onItemClickCallback: ItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<UserData>) {
        val diffUtils = MyDiffUtils(userList, items)
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        userList.clear()
        userList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private var binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userData: UserData) {
            binding.apply {
                imgItemPhoto.loadImageUrl(userData.avatarUrl.toString())
                tvItemUsername.text = userData.username
                tvItemName.text = userData.userType

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(userData)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size

}


