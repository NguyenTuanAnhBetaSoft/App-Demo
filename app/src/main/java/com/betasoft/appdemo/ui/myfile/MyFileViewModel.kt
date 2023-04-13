package com.betasoft.appdemo.ui.myfile

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.betasoft.appdemo.data.api.model.ImageLocal
import com.betasoft.appdemo.data.local.roomDb.dao.ImageLocalDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyFileViewModel @Inject constructor(
    private val imageLocalDao: ImageLocalDao
) :
    ViewModel() {

    //val allImageLocalLiveData = MutableLiveData<DataResponse<List<ImageLocal>?>>(DataResponse.DataIdle())

    fun getAllImageLocal(): Flow<PagingData<ImageLocal>> = Pager(
        config = PagingConfig(100, enablePlaceholders = false),
        pagingSourceFactory = { imageLocalDao.getAllImageLocal() }
    ).flow

    fun searchImageLocal(searchQuery: String): Flow<PagingData<ImageLocal>> = Pager(
        config = PagingConfig(100, enablePlaceholders = false),
        pagingSourceFactory = { imageLocalDao.searchPlaylist(searchQuery) }
    ).flow


    /*fun getAllImageLocal(isRefresh: Boolean) {
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
    }*/

}