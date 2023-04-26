package com.betasoft.appdemo.extensions

import android.util.Size
import androidx.exifinterface.media.ExifInterface

//fun ExifInterface.removeValues() {
//    val attributes = arrayListOf(
//        ExifInterface.TAG_IMAGE_LENGTH,
//        ExifInterface.TAG_IMAGE_WIDTH,
//
//
//}


fun ExifInterface.widthAndHeightImage(): Size {

    val width: Int = getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
    val height: Int = getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)
    return Size(width, height)

}