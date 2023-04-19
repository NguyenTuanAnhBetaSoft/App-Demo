package com.betasoft.appdemo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "imageLocal")
data class ImageLocal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("imageId")
    val imageId: Long = 0,
    val filePath: String = "",
    val nameAuthor: String = "",
    val prompt: String = "",
    val fileName: String = ""
): Parcelable
