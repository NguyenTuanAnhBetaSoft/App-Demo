package com.betasoft.appdemo.ui.openart

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.*
import com.betasoft.appdemo.data.api.model.ImageResult
import com.betasoft.appdemo.data.api.responseremote.ItemsItem
import com.betasoft.appdemo.data.repository.RemoteRepository
import com.betasoft.appdemo.data.response.DataResponse
import com.betasoft.appdemo.data.response.LoadingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val remoteRepository: RemoteRepository) :
    ViewModel() {
    private var curPage = 0
    val dataAppLiveData = MutableLiveData<DataResponse<ImageResult>>(DataResponse.DataIdle())

    val isLoadingMore: LiveData<Boolean> = Transformations.map(dataAppLiveData) {
        dataAppLiveData.value!!.loadingStatus == LoadingStatus.LoadingMore
    }

    val isLoading: LiveData<Boolean> = Transformations.map(dataAppLiveData) {
        dataAppLiveData.value!!.loadingStatus == LoadingStatus.Loading
    }

    val downloadImageLiveData = MutableLiveData<DataResponse<Bitmap>>(DataResponse.DataIdle())


    /*@Suppress("UNCHECKED_CAST")
    fun fetchImageList(isLoadMore: Boolean, nextCursor: String) {
        if (dataAppLiveData.value!!.loadingStatus != LoadingStatus.Loading
            && dataAppLiveData.value!!.loadingStatus != LoadingStatus.LoadingMore
        ) {
            if (!isLoadMore) {
                if (curPage > 1) {
                    dataAppLiveData.value = DataResponse.DataLoading(LoadingStatus.Refresh)
                } else {
                    dataAppLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
                }
                curPage = 1
            } else {
                dataAppLiveData.value = DataResponse.DataLoading(LoadingStatus.LoadingMore)

            }
            viewModelScope.launch {
                val result = remoteRepository.fetchImagePagingList(
                    nextCursor
                )
                if (result != null) {
                    dataAppLiveData.value = DataResponse.DataSuccess(
                        ImageResult(
                            curPage,
                            result.items as List<ItemsItem>?, result.nextCursor
                        )
                    )
                    curPage++
                } else {
                    dataAppLiveData.value = DataResponse.DataError(null)
                }
            }
        }

    }*/

    @Suppress("UNCHECKED_CAST")
    fun fetchImageList(isLoadMore: Boolean, cursor: String) {
        if (dataAppLiveData.value!!.loadingStatus != LoadingStatus.Loading
            && dataAppLiveData.value!!.loadingStatus != LoadingStatus.LoadingMore
        ) {
            if (!isLoadMore) {
                dataAppLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
                viewModelScope.launch {
                    val result = remoteRepository.fetchImageList(
                    )
                    if (result != null) {
                        curPage++
                        dataAppLiveData.value = DataResponse.DataSuccess(
                            ImageResult(
                                curPage,
                                result.items as List<ItemsItem>?, result.nextCursor
                            )
                        )

                    } else {
                        dataAppLiveData.value = DataResponse.DataError(null)
                    }
                }

            } else {
                dataAppLiveData.value = DataResponse.DataLoading(LoadingStatus.LoadingMore)
                viewModelScope.launch {
                    val result = remoteRepository.fetchImagePagingList(
                        cursor
                    )
                    if (result != null) {
                        curPage++
                        dataAppLiveData.postValue(
                            DataResponse.DataSuccess(
                                ImageResult(
                                    curPage,
                                    result.items as List<ItemsItem>?, result.nextCursor
                                )
                            )
                        )

                    } else {
                        dataAppLiveData.postValue(DataResponse.DataError(null))
                    }
                }
            }

        }

    }

     fun downloadImageUrl(
        url: String,
        name: String,
        haveSave: Boolean,
        context: Context
    ) {
        downloadImageLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
        viewModelScope.launch {
            val result = remoteRepository.downloadImageUrl(url, name, haveSave, context)
            Log.d("asdfdsf", "result = $result")
            if (result != null) {
                downloadImageLiveData.postValue(DataResponse.DataSuccess(result))
            } else {
                downloadImageLiveData.postValue(DataResponse.DataError(null))
            }
        }
    }

}