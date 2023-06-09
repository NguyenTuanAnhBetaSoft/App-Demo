package com.betasoft.appdemo.data.repository

import android.content.ContentUris
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.betasoft.appdemo.App
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.di.AppContext
import com.betasoft.appdemo.utils.AppConfig
import com.betasoft.appdemo.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImagesPagingSource @Inject constructor(
    @AppContext private val context: App
) : PagingSource<Int, MediaModel>() {

    private val projection =
        arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_MODIFIED
        )
    private val cursor = when {
        Utils.isAndroidQ() -> {
            context.contentResolver.query(
                AppConfig.IMAGES_MEDIA_URI,
                null,
                null,
                null,
                "_size DESC"
            )
        }

        else -> {
            context.contentResolver.query(
                AppConfig.IMAGES_MEDIA_URI,
                projection,
                null,
                null,
                "_size DESC"
            )
        }
    }

    private val idColumn = cursor?.getColumnIndexOrThrow(projection[0])
    private val albumIdColumn = cursor?.getColumnIndexOrThrow(projection[2])
    private val albumNameColumn =
        cursor?.getColumnIndexOrThrow(projection[1])
    private val pathColumn =
        cursor?.getColumnIndexOrThrow(projection[4])
    private val timeColumn = cursor?.getColumnIndexOrThrow(projection[3])
    private val sizeColumn = cursor?.getColumnIndexOrThrow(projection[5])
    private val dateColumn = cursor?.getColumnIndexOrThrow(projection[6])

    override fun getRefreshKey(state: PagingState<Int, MediaModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaModel> {
        val position = params.key ?: -1
        return try {
            val images: MutableList<MediaModel> = mutableListOf()
            getListFromUri(
                imagesOffset = position,
                imagesLimit = params.loadSize
            ).collect { imageDataList ->
                images.addAll(imageDataList)
            }

            LoadResult.Page(
                data = images,
                prevKey = if (position == -1) null else position,
                nextKey = if (images.isEmpty()) null else position + params.loadSize
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    private suspend fun getListFromUri(
        imagesOffset: Int,
        imagesLimit: Int,
    ): Flow<List<MediaModel>> = flow {
        val list = mutableListOf<MediaModel>()
        withContext(Dispatchers.IO) {
            cursor?.let {

                it.moveToPosition(imagesOffset)
                while (it.moveToNext() && it.position <= imagesOffset + imagesLimit) {
                    // Get values of columns for a given.
                    val path = it.getString(pathColumn!!) ?: "Unknown"
                    val file = File(path)
                    val id = it.getLong(idColumn!!)
                    val name = file.name
                    val albumId =
                        it.getLong(albumIdColumn!!)
                    val albumName =
                        it.getString(albumNameColumn!!) ?: "Unknown"
                    val time = it.getLong(timeColumn!!)

                    val imageSize = cursor.getLong(sizeColumn!!)
                    val modifiedDate = cursor.getLong(dateColumn!!)
                    val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                    val day = sdf.format(modifiedDate * 1000)

                    if (file.isHidden or file.isDirectory or !file.exists()) continue

                    val mediaModel = MediaModel(
                        id = id, uri = ContentUris.withAppendedId(
                            AppConfig.IMAGES_MEDIA_URI,
                            id
                        ), title = name, albumId = albumId, albumName = albumName, time = time, file = file, size = imageSize
                    ).apply {
                        duration = 0L
                    }
                    list.add(mediaModel)
                }
            }
        }
        emit(list)

    }


}