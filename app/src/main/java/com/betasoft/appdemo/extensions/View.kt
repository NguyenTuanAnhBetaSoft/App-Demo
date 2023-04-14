package com.betasoft.appdemo.extensions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setMargins

fun View.setOnSingClickListener(onClick: (View) -> Unit) {
    setOnClickListener(object : OnSingleClickListener() {
        override fun onSingleClick(view: View) {
            onClick.invoke(view)
        }
    })
}

fun View.setMargin(int: Int) {
    val layout = layoutParams as ViewGroup.MarginLayoutParams

    layout.setMargins(int)
    layoutParams = layout
}

//context
fun Context.getDimenInt(number: Int) = resources.getDimension(number)
fun Context.getDimenResources(int: Int): Int {
    return getDimenInt(int).toInt()
}