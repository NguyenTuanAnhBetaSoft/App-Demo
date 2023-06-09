package com.betasoft.appdemo.view.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.betasoft.appdemo.App
import com.betasoft.appdemo.data.model.ImageLocal
import com.betasoft.appdemo.data.model.ImageResult
import com.betasoft.appdemo.data.api.responseremote.ItemsItem
import com.betasoft.appdemo.data.repository.LocalRepository
import com.betasoft.appdemo.data.repository.RemoteRepository
import com.betasoft.appdemo.data.response.DataResponse
import com.betasoft.appdemo.data.response.LoadingStatus
import com.betasoft.appdemo.di.AppContext
import com.betasoft.appdemo.utils.checknetwork.ConnectivityObserver
import com.betasoft.appdemo.utils.checknetwork.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    @AppContext private val context: App
) :
    ViewModel() {

    private val  networkConnectivityObserver = NetworkConnectivityObserver(context)

    fun networkObserve(): Flow<ConnectivityObserver.Status> {
        return flow {
            networkConnectivityObserver.observe().collect{
                emit(it)
            }
        }
    }


    private var curPage = 1
    val dataAppLiveData = MutableLiveData<DataResponse<ImageResult>>(DataResponse.DataIdle())

    val isLoadingMore: LiveData<Boolean> = Transformations.map(dataAppLiveData) {
        dataAppLiveData.value!!.loadingStatus == LoadingStatus.LoadingMore
    }

    val isLoading: LiveData<Boolean> = Transformations.map(dataAppLiveData) {
        dataAppLiveData.value!!.loadingStatus == LoadingStatus.Loading
    }

    val isError: LiveData<Boolean> = Transformations.map(dataAppLiveData) {
        dataAppLiveData.value!!.loadingStatus == LoadingStatus.Error
    }

    val downloadImageLiveData = MutableLiveData<DataResponse<ImageLocal>?>(DataResponse.DataIdle())
    val downloadImagesLiveData =
        MutableLiveData<DataResponse<List<ImageLocal>?>>(DataResponse.DataIdle())

    val listItemSelectedLiveData = MutableLiveData<List<ItemsItem>?>()

    val isSelectLiveData = MutableLiveData(false)


    private var jobFetchImageList: Job? = null
    private var jobDownloadImageUrl: Job? = null
    private var jobDownloadImagesUrl: Job? = null
    private var jobInsertImageLocal: Job? = null

    fun canJobFetchImageList() {
        jobFetchImageList?.cancel()
    }

    fun canJobDownloadImageUrl() {
        jobDownloadImageUrl?.cancel()
    }

    fun canJobDownloadImagesUrl() {
        jobDownloadImagesUrl?.cancel()
    }

    fun canJobInsertImageLocal() {
        jobInsertImageLocal?.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    fun fetchImageList(isLoadMore: Boolean, cursor: String) {
        if (dataAppLiveData.value!!.loadingStatus != LoadingStatus.Loading
            && dataAppLiveData.value!!.loadingStatus != LoadingStatus.LoadingMore
            && dataAppLiveData.value!!.loadingStatus != LoadingStatus.Refresh
        ) {
            if (!isLoadMore) {
                if (curPage > 1) {
                    dataAppLiveData.value = DataResponse.DataLoading(LoadingStatus.Refresh)
                } else {
                    dataAppLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
                }
                curPage = 1
                jobFetchImageList = viewModelScope.launch {
                    val result = remoteRepository.fetchImageList(
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

            } else {
                dataAppLiveData.value = DataResponse.DataLoading(LoadingStatus.LoadingMore)
                jobFetchImageList = viewModelScope.launch {
                    val result = remoteRepository.fetchImagePagingList(
                        cursor
                    )
                    if (result != null) {
                        dataAppLiveData.postValue(
                            DataResponse.DataSuccess(
                                ImageResult(
                                    curPage,
                                    result.items as List<ItemsItem>?, result.nextCursor
                                )
                            )
                        )
                        curPage++
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
        context: Context,
        nameAuthor: String,
        prompt: String,
    ) {
        downloadImageLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
        jobDownloadImageUrl = viewModelScope.launch {
            val result = localRepository.downloadImageUrl(url, name, haveSave, context)
            if (result != null && result.isNotEmpty()) {
                downloadImageLiveData.postValue(
                    DataResponse.DataSuccess(
                        ImageLocal(
                            filePath = result,
                            nameAuthor = nameAuthor,
                            prompt = prompt,
                            fileName = name
                        )
                    )
                )
            } else {
                downloadImageLiveData.postValue(DataResponse.DataError(null))
            }
        }
    }

    fun downloadImagesUrl(
        item: List<ItemsItem>,
        haveSave: Boolean,
        context: Context
    ) {
        downloadImagesLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
        jobDownloadImagesUrl = viewModelScope.launch {
            val result = localRepository.downloadImagesUrl(item, haveSave, context)
            if (result != null && result.isNotEmpty()) {
                val listImageLocal = mutableListOf<ImageLocal>()
                result.forEachIndexed { index, s ->
                    if (s.isNotEmpty()) {
                        val itemImageLocal = ImageLocal(
                            filePath = s,
                            nameAuthor = item[index].userProfile!!.name.toString(),
                            prompt = item[index].prompt.toString(),
                            fileName = item[index].id.toString()
                        )
                        listImageLocal.add(itemImageLocal)
                    }
                }
                downloadImagesLiveData.postValue(DataResponse.DataSuccess(listImageLocal))
            } else {
                downloadImageLiveData.postValue(DataResponse.DataError(null))
            }
        }
    }

    fun insertImageLocal(imageLocal: ImageLocal) {
        jobInsertImageLocal = viewModelScope.launch {
            try {
                localRepository.insertImageLocal(imageLocal)
            } catch (_: Exception) {

            }
        }

    }

    fun insertImagesLocal(imagesLocal: List<ImageLocal>) {
        jobInsertImageLocal = viewModelScope.launch {
            try {
                imagesLocal.forEachIndexed { _, imageLocal ->
                    localRepository.insertImageLocal(imageLocal)
                }

            } catch (_: Exception) {

            }
        }

    }


    fun updateListItemSelected(list: List<ItemsItem>) {
        viewModelScope.launch {
            listItemSelectedLiveData.postValue(list)
        }
    }

    fun isSelect(isSelect: Boolean) {
        isSelectLiveData.value = isSelect
    }


}