package com.betasoft.appdemo.ui.base.popup

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActionModel(val icon: Int, val title: String) : Parcelable
