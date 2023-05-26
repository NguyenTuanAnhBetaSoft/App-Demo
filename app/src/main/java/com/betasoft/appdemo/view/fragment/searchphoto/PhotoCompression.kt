package com.betasoft.appdemo.view.fragment.searchphoto

import android.net.Uri
import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.io.File

@Keep
@Parcelize
data class PhotoCompression(
    var id: Long = 0,
    var uri: Uri,
    val title: String = "",
    val albumId: Long = 0,
    val albumName: String = "",
    val file: File?,
    val size: Long = 0

) : Parcelable  {
    var childCount: Int = 0
}
