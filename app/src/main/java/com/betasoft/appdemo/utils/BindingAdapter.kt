package com.betasoft.appdemo.utils

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.betasoft.appdemo.R
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("android:loadImageByUrl")
fun ShapeableImageView.loadImageByUrl(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_thumb_image)
        .error(R.drawable.ic_thumb_image)
        .into(this)
}


@BindingAdapter("android:loadImageByUri")
fun ImageView.loadImageByUri(uri: Any?) {
    if (uri is Uri) {
        Glide.with(this).load(uri)
            .placeholder(R.drawable.ic_thumb_image)
            .error(R.drawable.ic_thumb_image).into(this)
    } else {
        Glide.with(this).load(R.drawable.ic_thumb_image)
            .placeholder(R.drawable.ic_thumb_image)
            .error(R.drawable.ic_thumb_image).into(this)
    }
}

@BindingAdapter("app:setTextNameAuthor")
fun TextView.setTextNameAuthor(string: String) {
    text = buildString {
        append("Author: ")
        append(string)
    }
}
@BindingAdapter("app:setTextNameFile")
fun TextView.setTextNameFile(string: String) {
    text = buildString {
        append("File: ")
        append(string)
        append(".jpg")
    }
}




