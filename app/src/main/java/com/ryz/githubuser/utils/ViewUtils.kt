package com.ryz.githubuser.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ryz.githubuser.R

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun ImageView.loadImageUrl(url: String) {
    Glide.with(this.context)
        .load(url)
        .circleCrop()
        .placeholder(R.drawable.ic_baseline_refresh_24)
        .error(R.drawable.ic_baseline_broken_image_24)
        .into(this)
}