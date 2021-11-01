package com.ryz.githubuser.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val id: Int? = null,
    val username: String? = null,
    val userType: String? = null,
    val avatarUrl: String? = null
): Parcelable
