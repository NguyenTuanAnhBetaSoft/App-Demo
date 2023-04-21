package com.betasoft.appdemo.data.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class CastMediaModel(
    var listMedeaModel : List<MediaModel>
) : Parcelable
