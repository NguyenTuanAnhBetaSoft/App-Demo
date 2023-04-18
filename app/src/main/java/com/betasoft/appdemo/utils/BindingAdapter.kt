package com.betasoft.appdemo.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.betasoft.appdemo.R
import com.betasoft.appdemo.li.TouchImageView
import com.betasoft.appdemo.ui.base.popup.ActionModel
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("android:loadImageByUrl")
fun ShapeableImageView.loadImageByUrl(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_image_empty)
        .error(R.drawable.ic_image_empty)
        .into(this)
}


@BindingAdapter("android:loadImageByUri")
fun ImageView.loadImageByUri(uri: Any?) {
    if (uri is Uri) {
        Glide.with(this).load(uri)
            .placeholder(R.drawable.ic_image_empty)
            .error(R.drawable.ic_image_empty).into(this)
    } else {
        Glide.with(this).load(R.drawable.ic_image_empty)
            .placeholder(R.drawable.ic_image_empty)
            .error(R.drawable.ic_image_empty).into(this)
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

@BindingAdapter("android:bindTouchImage")
fun TouchImageView.bindTouchImage(uri: String?) {
    if (uri != null) {
        Glide.with(this)
            .load(uri)
            .placeholder(androidx.appcompat.R.color.background_floating_material_dark)
            .error(androidx.appcompat.R.color.background_floating_material_dark)
            .into(this)
    } else {
        Glide.with(this).load(androidx.appcompat.R.color.background_floating_material_dark)
            .error(androidx.appcompat.R.color.background_floating_material_dark)
            .into(this)
    }

}

@BindingAdapter("android:iconForAction")
fun ImageView.iconForAction(actionModel: ActionModel) {
    if (actionModel.icon <0) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        setImageResource(actionModel.icon)
    }

}




