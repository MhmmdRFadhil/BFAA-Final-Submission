package com.ryz.githubuser.interfaces

import com.ryz.githubuser.model.UserData

interface ItemClickCallback {
    fun onItemClicked(data: UserData)
}