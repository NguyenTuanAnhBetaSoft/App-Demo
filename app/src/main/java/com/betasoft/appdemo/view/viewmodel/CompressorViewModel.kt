package com.betasoft.appdemo.view.viewmodel

import android.app.RecoverableSecurityException
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betasoft.appdemo.App
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.data.repository.CompressRepository
import com.betasoft.appdemo.data.response.DataResponse
import com.betasoft.appdemo.data.response.LoadingStatus
import com.betasoft.appdemo.di.AppContext
import com.betasoft.appdemo.utils.compress.StorageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CompressorViewModel @Inject constructor(
    @AppContext private val context: App,
    private val compressRepository: CompressRepository
) :
    ViewModel() {

    private var compressCacheJob: Job? = null
    private var compressJob: Job? = null

    val compressCacheImageListLiveData =
        MutableLiveData<DataResponse<List<File>?>>(DataResponse.DataIdle())

    val compressImageListLiveData =
        MutableLiveData<DataResponse<Boolean>>(DataResponse.DataIdle())

    fun cancelCompressCacheJob() {
        compressCacheJob?.cancel()
    }

    fun cancelCompressJob() {
        compressJob?.cancel()
    }

    fun compressCache(
        listImageRoot: List<MediaModel>,
        quality: Int
    ) {
        compressCacheImageListLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
        compressCacheJob = viewModelScope.launch {
            try {
                File(context.cacheDir, "compressor").deleteRecursively()
                val result = compressRepository.compressCache(listImageRoot, quality)
                if (result!!.isNotEmpty()) {
                    compressCacheImageListLiveData.postValue(
                        DataResponse.DataSuccess(
                            result
                        )
                    )
                } else {
                    compressCacheImageListLiveData.postValue(DataResponse.DataError(null))
                }
            } catch (_: Exception) {

            }
        }
    }

    fun compressImages(
        listImageCache: List<File>,
        isKeepImage: Boolean,
        listImageRoot: List<MediaModel>
    ) {
        compressImageListLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)

        compressJob = viewModelScope.launch(Dispatchers.Default) {
            try {
                listImageCache.forEachIndexed { index, file ->
                    StorageUtils().saveImage(
                        context = context, image = file, isKeepImage = isKeepImage, rootFile = listImageRoot[index].file!!
                    )
                }
                compressImageListLiveData.postValue(DataResponse.DataSuccess(true))
            } catch (_ : Exception) {
                compressImageListLiveData.postValue(DataResponse.DataError(null))
            }
        }
    }

    fun deleteCache(context: Context) {
        viewModelScope.launch {
            File(context.cacheDir, "compressor").deleteRecursively()
        }
    }


}