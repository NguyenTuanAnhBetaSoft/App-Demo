package com.betasoft.appdemo.data.repository

import android.content.Context
import com.betasoft.appdemo.data.model.ImageLocal
import com.betasoft.appdemo.data.api.responseremote.ItemsItem
import com.betasoft.appdemo.data.local.roomDb.dao.ImageLocalDao
import com.betasoft.appdemo.utils.download.DownloadUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(private val imageLocalDao: ImageLocalDao) {

    suspend fun insertImageLocal(image: ImageLocal) = withContext(Dispatchers.Default) {
        try {
            imageLocalDao.insert(image)
        } catch (_: Exception) {

        }

    }

    suspend fun deleteImageLocal(image: ImageLocal) = withContext(Dispatchers.Default) {
        try {
            imageLocalDao.delete(image)
        } catch (_: Exception) {

        }
    }

    suspend fun downloadImageUrl(
        url: String,
        name: String,
        haveSave: Boolean,
        context: Context
    ) = withContext(Dispatchers.IO) {
        try {
            DownloadUrl.download(80,url, name, haveSave, context)
        } catch (ex: Exception) {
            return@withContext null
        }
    }

    suspend fun downloadImagesUrl(
        item: List<ItemsItem>,
        haveSave: Boolean,
        context: Context
    ) = withContext(Dispatchers.IO) {
        val listDownLoad = arrayListOf<String>()
        try {
            item.forEachIndexed { _, itemsItem ->
                val result = DownloadUrl.download(
                    80,
                    itemsItem.image_url.toString(),
                    itemsItem.id.toString(),
                    haveSave,
                    context
                )

                listDownLoad.add(result)

            }
            listDownLoad
        } catch (ex: Exception) {
            return@withContext null
        }

    }
}