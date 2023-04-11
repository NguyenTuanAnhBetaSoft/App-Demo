package com.betasoft.appdemo.data.api.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class ImageLocal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("imageId")
    val imageId: Long = 0,
    val filePath: String = "",
    val nameAuthor: String = "",
    val prompt: String = ""
): Parcelable
