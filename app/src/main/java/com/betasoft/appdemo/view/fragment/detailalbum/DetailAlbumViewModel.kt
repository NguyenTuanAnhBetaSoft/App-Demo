package com.betasoft.appdemo.view.fragment.detailalbum

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.betasoft.appdemo.App
import com.betasoft.appdemo.di.AppContext
import com.betasoft.appdemo.view.fragment.album.AlbumRepository
import com.betasoft.appdemo.view.fragment.searchphoto.PhotoCompression
import com.betasoft.appdemo.view.fragment.searchphoto.PhotosPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailAlbumViewModel @Inject constructor(
    private val photosPagingSource: PhotosPagingSource,
    @AppContext private val context: App,
) :
    ViewModel() {

    var listPhotos: Flow<PagingData<PhotoCompression>> = Pager(
        config = PagingConfig(
            pageSize = 20
        ),
        pagingSourceFactory = { photosPagingSource }
    ).flow.cachedIn(viewModelScope)

    fun searchPhotosByBucketId(bucketId: String) {
        photosPagingSource.setBucketIdQuery(bucketId)
        listPhotos = Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = { photosPagingSource }
        ).flow.cachedIn(viewModelScope)
    }



}