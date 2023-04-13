package com.betasoft.appdemo.li

import android.widget.ImageView

internal data class ZoomVariables(
    var scale: Float,
    var focusX: Float,
    var focusY: Float,
    var scaleType: ImageView.ScaleType?,
)