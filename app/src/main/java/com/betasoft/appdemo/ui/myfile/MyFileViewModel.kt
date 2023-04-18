package com.betasoft.appdemo.ui.myfile

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.betasoft.appdemo.data.api.model.ImageLocal
import com.betasoft.appdemo.data.local.roomDb.dao.ImageLocalDao
import com.betasoft.appdemo.data.repository.LocalRepository
import com.betasoft.appdemo.extensions.shareMultiple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyFileViewModel @Inject constructor(
    private val imageLocalDao: ImageLocalDao, private val localRepository: LocalRepository
) :
    ViewModel() {

    fun getAllImageLocal(): Flow<PagingData<ImageLocal>> = Pager(
        config = PagingConfig(100, enablePlaceholders = false),
        pagingSourceFactory = { imageLocalDao.getAllImageLocal() }
    ).flow

    fun searchImageLocal(searchQuery: String): Flow<PagingData<ImageLocal>> = Pager(
        config = PagingConfig(100, enablePlaceholders = false),
        pagingSourceFactory = { imageLocalDao.searchPlaylist(searchQuery) }
    ).flow

    fun deleteImage(imageLocal: ImageLocal) {
        viewModelScope.launch {
            localRepository.deleteImageLocal(imageLocal)
        }
    }

    fun shareFiles(
        activity: Activity,
        listFile: List<ImageLocal>
    ) {
        val listPath = listFile.flatMap {
            val list = mutableListOf<String>()
            val filePath = File(it.filePath)
            list.add(filePath.path)
            list
        }

        activity.shareMultiple(listPath)
    }

    /*fun shareFiles(
        file: String, context: Context
    ) {
        Utils.openShareWindow(context, file)
    }*/

}