package com.betasoft.appdemo.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.data.repository.ImagesPagingSource
import com.betasoft.appdemo.data.repository.VideosPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaLocalViewModel @Inject constructor(
    private val imagesPagingSource: ImagesPagingSource,
    private val videosPagingSource: VideosPagingSource
) :
    ViewModel() {

    val listItemSelectedLiveData = MutableLiveData<List<MediaModel>?>()

    val isSelectLiveData = MutableLiveData(false)

    fun isSelect(isSelect: Boolean) {
        isSelectLiveData.value = isSelect
    }

    fun updateListItemSelected(list: List<MediaModel>) {
        viewModelScope.launch {
            listItemSelectedLiveData.postValue(list)
        }
    }

    fun fetAllImages(): Flow<PagingData<MediaModel>> = Pager(
        config = PagingConfig(
            pageSize = 80,  //80 120
            initialLoadSize = 120
        ),
        pagingSourceFactory = { imagesPagingSource }
    ).flow

    fun fetAllVideos(): Flow<PagingData<MediaModel>> = Pager(
        config = PagingConfig(
            pageSize = 80,  //80 120
            initialLoadSize = 120
        ),
        pagingSourceFactory = { videosPagingSource }
    ).flow


}