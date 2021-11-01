package com.ryz.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ryz.githubuser.databinding.ItemRowUserFavoriteBinding
import com.ryz.githubuser.interfaces.ItemClickCallback
import com.ryz.githubuser.model.UserData
import com.ryz.githubuser.utils.MyDiffUtils
import com.ryz.githubuser.utils.loadImageUrl

class UserFavoriteAdapter : RecyclerView.Adapter<UserFavoriteAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: ItemClickCallback
    private val userFavorite = ArrayList<UserData>()

    fun setOnItemClickCallback(onItemClickCallback: ItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<UserData>) {
        val diffUtils = MyDiffUtils(userFavorite, items)
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        userFavorite.clear()
        userFavorite.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private var binding: ItemRowUserFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userFavorite: UserData) {
            binding.apply {
                imgItemPhotoFavorite.loadImageUrl(userFavorite.avatarUrl.toString())
                tvItemUsernameFavorite.text = userFavorite.username
                tvItemNameFavorite.text = userFavorite.userType

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(userFavorite)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowUserFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userFavorite[position])
    }

    override fun getItemCount(): Int = userFavorite.size
}