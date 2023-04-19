package com.betasoft.appdemo.ui.media

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.betasoft.appdemo.data.model.ImageLocal
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.data.repository.ImagesPagingSource
import com.betasoft.appdemo.data.repository.MediaLocalRepository
import com.betasoft.appdemo.data.response.DataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MediaLocalViewModel @Inject constructor(
    private val mediaLocalRepository: MediaLocalRepository,
    private val imagesPagingSource: ImagesPagingSource
) :
    ViewModel() {

    val mediaLocalLiveData =
        MutableLiveData<DataResponse<List<MediaModel>?>>(DataResponse.DataIdle())


    /*fun fetAllImage() {
        mediaLocalLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
        viewModelScope.launch {
            val result = mediaLocalRepository.getAllImage()
            if (result != null && result.isNotEmpty()) {
                mediaLocalLiveData.postValue(DataResponse.DataSuccess(result))

            }else {
                mediaLocalLiveData.postValue(DataResponse.DataError(null))
            }
        }
    }*/

    fun fetAllImage(): Flow<PagingData<MediaModel>> = Pager(
        config = PagingConfig(pageSize = 1,  //80 120
            initialLoadSize = 2),
        pagingSourceFactory = { imagesPagingSource }
    ).flow




}