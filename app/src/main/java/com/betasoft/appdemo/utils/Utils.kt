package com.betasoft.appdemo.utils

import android.os.Build

object Utils {
    fun isAndroidQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
}