package com.betasoft.appdemo.ui.myfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.betasoft.appdemo.data.api.model.ImageLocal
import com.betasoft.appdemo.data.local.roomDb.dao.ImageLocalDao
import com.betasoft.appdemo.data.repository.LocalRepository
import com.betasoft.appdemo.data.response.DataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyFileViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val imageLocalDao: ImageLocalDao
) :
    ViewModel() {

    //val allImageLocalLiveData = MutableLiveData<DataResponse<List<ImageLocal>?>>(DataResponse.DataIdle())

    val hintLiveData = MutableLiveData(false)

    fun getAllImageLocal(): Flow<PagingData<ImageLocal>> = Pager(
        config = PagingConfig(100, enablePlaceholders = false),
        pagingSourceFactory = { imageLocalDao.getAllImageLocal() }
    ).flow

    fun searchImageLocal(searchQuery: String): Flow<PagingData<ImageLocal>> = Pager(
        config = PagingConfig(100, enablePlaceholders = false),
        pagingSourceFactory = { imageLocalDao.searchPlaylist(searchQuery) }
    ).flow

    fun hintBottomNav(isHint: Boolean) {
        hintLiveData.value = isHint
    }

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