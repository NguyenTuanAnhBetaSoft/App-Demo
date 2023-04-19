package com.betasoft.appdemo.data.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class MediaModel(
    var id: Long = 0,
    var uri: Uri,
    val title: String,
    val albumId: Long,
    val albumName: String,
    val time: Long,
    val file: File?,
) : Parcelable {
    var childCount: Int = 0
    var isCheck: Boolean = false
    var duration: Long = 0
}
