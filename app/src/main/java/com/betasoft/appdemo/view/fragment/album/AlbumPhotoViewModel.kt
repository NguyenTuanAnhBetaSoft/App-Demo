package com.betasoft.appdemo.view.fragment.album

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.betasoft.appdemo.App
import com.betasoft.appdemo.data.response.DataResponse
import com.betasoft.appdemo.di.AppContext
import com.betasoft.appdemo.view.fragment.album.AlbumRepository
import com.betasoft.appdemo.view.fragment.searchphoto.Album
import com.betasoft.appdemo.view.fragment.searchphoto.PhotosPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumPhotoViewModel @Inject constructor(
    private val photosPagingSource: PhotosPagingSource,
    @AppContext private val context: App,
    private val albumRepository: AlbumRepository
) :
    ViewModel() {

    var albumsLiveData = MutableLiveData<List<Album>>()


    fun loadAlbums() {
        viewModelScope.launch {
            val albums = albumRepository.getAlbums()
            albumsLiveData.postValue(albums)
            Log.d("423423423", "album $albums")

        }
    }

}