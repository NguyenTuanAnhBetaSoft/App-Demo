package com.betasoft.appdemo.view.fragment.searchphoto

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Album(
    val id: Long,
    val name: String,
    var firstPhoto: PhotoCompression?,
    var totalImages: Int = 0
): Parcelable
