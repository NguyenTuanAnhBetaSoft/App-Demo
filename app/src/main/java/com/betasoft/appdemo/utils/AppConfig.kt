package com.betasoft.appdemo.utils

import android.net.Uri
import android.provider.MediaStore
import com.betasoft.appdemo.utils.Utils.isAndroidQ

object AppConfig {
    val VIDEO_MEDIA_URI: Uri = if (isAndroidQ()) {
        MediaStore.Video.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        )
    } else MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    val IMAGES_MEDIA_URI: Uri = if (isAndroidQ()) {
        MediaStore.Images.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        )
    } else MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    const val FORMAT_DURATION_UNDER_HOUR = "%2d:%02d"
    const val FORMAT_DURATION_UPPER_HOUR = "%2d:%02d:%02d"
}