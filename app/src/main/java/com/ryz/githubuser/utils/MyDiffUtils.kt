package com.ryz.githubuser.utils

import androidx.recyclerview.widget.DiffUtil
import com.ryz.githubuser.model.UserData

class MyDiffUtils(
    private val oldList: ArrayList<UserData>,
    private val newList: ArrayList<UserData>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }
            oldList[oldItemPosition].username != newList[newItemPosition].username -> {
                false
            }
            oldList[oldItemPosition].userType != newList[newItemPosition].userType -> {
                false
            }
            oldList[oldItemPosition].avatarUrl != newList[newItemPosition].avatarUrl -> {
                false
            }
            else -> true
        }
    }
}