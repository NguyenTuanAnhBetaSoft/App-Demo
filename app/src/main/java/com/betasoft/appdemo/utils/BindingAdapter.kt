package com.betasoft.appdemo.utils

import androidx.databinding.BindingAdapter
import com.betasoft.appdemo.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("android:loadImageByUrl")
fun ShapeableImageView.loadImageByUrl(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_launcher_background)
        .into(this)
}

@BindingAdapter("android:loadAvatarByUrl")
fun ShapeableImageView.loadAvatarByUrl(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_launcher_background)
        .into(this)
}

