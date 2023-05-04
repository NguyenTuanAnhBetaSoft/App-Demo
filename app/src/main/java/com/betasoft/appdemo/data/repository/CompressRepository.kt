package com.betasoft.appdemo.data.repository

import android.graphics.Bitmap
import com.betasoft.appdemo.App
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.di.AppContext
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


class CompressRepository @Inject constructor(@AppContext private val context: App) {
    suspend fun compressCache(listImage: List<MediaModel>, quality: Int): List<File>? =
        withContext(Dispatchers.Default) {
            try {
                val compressedImagePathList = mutableListOf<File>()
                for (i in listImage) {
                    val file = Compressor.compress(
                        context,
                        i.file!!
                    ) {
                        //destination(filePath.absoluteFile)
                        //resolution(1280, 720)
                        //calculateInSampleSize(BitmapFactory.Options(),50,50)
                        //resolution(507, 675)
                        //destination(file)
                        quality(quality)
                        format(Bitmap.CompressFormat.JPEG)
                        size(2_097_152) // 2 MB
                        //setCompressedImage()
                    }
                    if (file != null) {
                        compressedImagePathList.add(file)
                    }

                }
                return@withContext compressedImagePathList

            } catch (_: Exception) {
                return@withContext null
            }
        }
}