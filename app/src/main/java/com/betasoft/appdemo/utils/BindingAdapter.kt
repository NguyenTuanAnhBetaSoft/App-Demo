package com.betasoft.appdemo.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.extensions.setMargin
import com.betasoft.appdemo.utils.li.TouchImageView
import com.betasoft.appdemo.view.base.popup.ActionModel
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

@BindingAdapter("android:bindThumbnailFile")
fun ImageView.bindThumbnailFile(mediaModel: MediaModel?) {
    if (mediaModel != null) {
        Glide.with(this).load(mediaModel.file)
            .placeholder(R.drawable.ic_image_empty)
            .error(R.drawable.ic_image_empty).into(this)
    } else {
        Glide.with(this).load(R.drawable.ic_image_empty)
            .placeholder(R.drawable.ic_image_empty)
            .error(R.drawable.ic_image_empty).into(this)
    }

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

@BindingAdapter("android:setImageSelect")
fun ShapeableImageView.setImageSelect(position:Int) {

    this.scaleY = 1.16F
    this.scaleX = 1F
    this.strokeWidth = 10F
    this.setStrokeColorResource(R.color.white)

    this.setMargin(4)

}

@BindingAdapter("android:setImageUnSelect")
fun ShapeableImageView.setImageUnSelect(position:Int) {
    this.scaleY = 1F
    this.scaleX = 1F
    this.strokeWidth = 0F

    this.setMargin(4)
    //this.setPadding(4,20,4,20)


}



