package com.betasoft.appdemo.ui.myfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betasoft.appdemo.data.api.model.ImageLocal
import com.betasoft.appdemo.data.repository.LocalRepository
import com.betasoft.appdemo.data.response.DataResponse
import com.betasoft.appdemo.data.response.LoadingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyFileViewModel @Inject constructor(private val localRepository: LocalRepository) :
    ViewModel() {

    val allImageLocalLiveData = MutableLiveData<DataResponse<List<ImageLocal>?>>(DataResponse.DataIdle())

    private var jobGetAllImageLocal: Job? = null

    fun canJobGetAllImageLocal() {
        jobGetAllImageLocal?.cancel()
    }

    fun getAllImageLocal(isRefresh: Boolean) {
        if (isRefresh) {
            allImageLocalLiveData.value = DataResponse.DataLoading(LoadingStatus.Refresh)
        } else {
            allImageLocalLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
        }

        jobGetAllImageLocal = viewModelScope.launch {
            val result = localRepository.getAllImageLocal()
            if (result.isNotEmpty()) {
                allImageLocalLiveData.postValue(DataResponse.DataSuccess(result))
            } else {
                allImageLocalLiveData.postValue(DataResponse.DataError(null))
            }
        }
    }
}