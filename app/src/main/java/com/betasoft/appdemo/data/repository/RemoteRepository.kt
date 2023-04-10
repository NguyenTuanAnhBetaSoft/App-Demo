package com.betasoft.appdemo.data.repository

import android.content.Context
import com.betasoft.appdemo.data.api.RemoteServices
import com.betasoft.appdemo.data.api.responseremote.DataResponseRemote
import com.betasoft.appdemo.utils.download.DownloadUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository @Inject constructor(private val remoteServices: RemoteServices) {
    suspend fun fetchImagePagingList(cursor: String): DataResponseRemote? =
        withContext(Dispatchers.IO) {
            try {
                remoteServices.fetchImagePagingList(cursor)
            } catch (ex: Exception) {
                null
            }
        }

    suspend fun fetchImageList(): DataResponseRemote? =
        withContext(Dispatchers.IO) {
            try {
                remoteServices.fetchImageList()
            } catch (ex: Exception) {
                null
            }
        }

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