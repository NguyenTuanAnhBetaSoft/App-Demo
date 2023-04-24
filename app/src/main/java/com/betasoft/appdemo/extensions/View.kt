package com.betasoft.appdemo.extensions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.core.view.setMargins
import com.google.android.material.snackbar.Snackbar

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

fun View.hideKeyboard(context: Context) {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.showSnackBar(@StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snackBar = Snackbar.make(this, resources.getString(messageRes), length)
    snackBar.f()
    snackBar.show()
}