package com.betasoft.appdemo.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betasoft.appdemo.data.api.responseremote.DataResponseRemote
import com.betasoft.appdemo.data.repository.RemoteRepository
import com.betasoft.appdemo.data.response.DataResponse
import com.betasoft.appdemo.data.response.LoadingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val remoteRepository: RemoteRepository): ViewModel() {
    val dataAppLiveData = MutableLiveData<DataResponse<DataResponseRemote>?>(DataResponse.DataIdle())

    fun fetchImagePagingList(nextCursor: String) {
        dataAppLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
        viewModelScope.launch {
            val result = remoteRepository.fetchImagePagingList(nextCursor)

            if (result != null) {
                dataAppLiveData.postValue(DataResponse.DataSuccess(result))
            } else {
                dataAppLiveData.postValue(DataResponse.DataError(null))
            }
        }
    }

    fun fetchImageList() {
        dataAppLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
        viewModelScope.launch {
            val result = remoteRepository.fetchImageList()
            Log.d("fadsfds", "result = ${result.toString()}")
            if (result != null) {
                dataAppLiveData.postValue(DataResponse.DataSuccess(result))
            } else {
                dataAppLiveData.postValue(DataResponse.DataError(null))
            }

        }

    }

}