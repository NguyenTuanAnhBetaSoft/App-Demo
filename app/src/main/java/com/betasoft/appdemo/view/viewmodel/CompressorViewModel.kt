package com.betasoft.appdemo.view.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.data.response.DataResponse
import com.betasoft.appdemo.data.response.LoadingStatus
import com.betasoft.appdemo.utils.download.DownloadUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CompressorViewModel @Inject constructor(
) :
    ViewModel() {

    private var compressJob: Job? = null

    val compressImageListLiveData =
        MutableLiveData<DataResponse<List<File>?>>(DataResponse.DataIdle())

    fun cancelCompressJob() {
        compressJob?.cancel()
    }

    fun test(
        listImage: List<MediaModel>,
        isKeepImage: Boolean,
        quality: Int,
        context: Context
    ) {
        compressImageListLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
        compressJob = viewModelScope.launch(Dispatchers.Default) {
            try {
                val compressedImagePathList = mutableListOf<File>()
                for (i in listImage) {
                    var file: File?
                    file = Compressor.compress(
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

                    compressedImagePathList.add(file)
                }

                if (compressedImagePathList.isNotEmpty()) {
                    compressImageListLiveData.postValue(
                        DataResponse.DataSuccess(
                            compressedImagePathList
                        )
                    )
                }


            } catch (_: Exception) {

            }
        }
    }

    fun compressImages(
        imagePathList: List<File>,
        isKeepImage: Boolean,
        quality: Int,
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            Log.d("434343", "compressImgPahtList = $imagePathList")
            for (c in imagePathList) {
                saveImage(
                    nameImage = c.name,
                    file = c,
                    isKeepImage = isKeepImage,
                    quality = quality,
                    context = context
                )

            }
            File(context.cacheDir, "compressor").deleteRecursively()
        }
    }

    private fun saveImage(
        nameImage: String,
        file: File,
        isKeepImage: Boolean,
        quality: Int,
        context: Context
    ) {
        Log.d("54545454554", "name: $nameImage")
        val fileName = if (isKeepImage) {
            val now = Date()
            now.time.toString()
        } else {
            nameImage.substring(0, nameImage.length - 4)
        }

        DownloadUrl.saveImage(
            quality,
            file,
            fileName,
            true,
            context
        )
    }

    fun deleteCache(context: Context) {
        viewModelScope.launch {
            File(context.cacheDir, "compressor").deleteRecursively()
        }
    }

}