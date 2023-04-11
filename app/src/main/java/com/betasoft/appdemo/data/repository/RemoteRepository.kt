package com.betasoft.appdemo.data.repository

import com.betasoft.appdemo.data.api.RemoteServices
import com.betasoft.appdemo.data.api.responseremote.DataResponseRemote
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

}