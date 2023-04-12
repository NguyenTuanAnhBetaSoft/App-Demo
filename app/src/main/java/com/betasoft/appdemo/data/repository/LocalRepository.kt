package com.betasoft.appdemo.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.betasoft.appdemo.data.api.RemoteServices
import com.betasoft.appdemo.data.api.model.ImageLocal
import com.betasoft.appdemo.data.api.responseremote.DataResponseRemote
import com.betasoft.appdemo.data.local.roomDb.dao.ImageLocalDao
import com.betasoft.appdemo.utils.download.DownloadUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(private val imageLocalDao: ImageLocalDao) {

    suspend fun insertImageLocal(image: ImageLocal) = withContext(Dispatchers.Default) {
        imageLocalDao.insert(image)
    }

    /*suspend fun getAllImageLocal(): List<ImageLocal> = withContext(Dispatchers.Default) {
        imageLocalDao.getAllImageLocal()
    }*/

    /*fun getAllImageLocal() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = {imageLocalDao.getAllImageLocal()}
    ).liveData*/


    suspend fun downloadImageUrl(
        url: String,
        name: String,
        haveSave: Boolean,
        context: Context
    ) = withContext(Dispatchers.IO) {
        try {
            DownloadUrl.download(url, name, haveSave, context)
        } catch (ex: Exception) {
            return@withContext null
        }
    }
}