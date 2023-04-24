package com.betasoft.appdemo.view.base.popup

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActionModel(val icon: Int, val title: String) : Parcelable
