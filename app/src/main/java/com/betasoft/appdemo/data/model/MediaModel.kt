package com.betasoft.appdemo.data.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File
import java.time.Instant
import java.util.*

@Parcelize
data class MediaModel(
    var id: Long = 0,
    var uri: Uri,
    val title: String = "",
    val albumId: Long = 0,
    val albumName: String = "",
    val time: Long = 0,
    val file: File?,
    val size : Long = 0

    ) : Parcelable {
    var duration: Long = 0
}
