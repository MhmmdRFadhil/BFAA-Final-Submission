package com.ryz.githubuser.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDataDetail(
    val name: String? = null,
    val username: String? = null,
    val image: String? = null,
    val followers: String? = null,
    val following: String? = null,
    val company: String? = null,
    val location: String? = null,
    val repository: String? = null
) : Parcelable
