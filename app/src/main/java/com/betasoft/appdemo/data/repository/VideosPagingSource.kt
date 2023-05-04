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
class VideosPagingSource @Inject constructor(
    @AppContext private val context: App
) : PagingSource<Int, MediaModel>() {

    private val projection =
        arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_MODIFIED
        )
    private val cursor = when {
        Utils.isAndroidQ() -> {
            context.contentResolver.query(
                AppConfig.VIDEO_MEDIA_URI,
                null,
                null,
                null,
                "date_added DESC"
            )
        }

        else -> {
            context.contentResolver.query(
                AppConfig.VIDEO_MEDIA_URI,
                projection,
                null,
                null,
                "date_added DESC"
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
    private val durationColumn = cursor?.getColumnIndexOrThrow(projection[5])
    private val sizeColumn = cursor?.getColumnIndexOrThrow(projection[6])
    private val dateColumn = cursor?.getColumnIndexOrThrow(projection[7])


    override fun getRefreshKey(state: PagingState<Int, MediaModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaModel> {
        val position = params.key ?: -1
        return try {
            val videos: MutableList<MediaModel> = mutableListOf()
            getListFromUri(
                imagesOffset = position,
                imagesLimit = params.loadSize,
            ).collect { imageDataList ->
                videos.addAll(imageDataList)
            }

            LoadResult.Page(
                data = videos,
                prevKey = if (position == -1) null else position,
                nextKey = if (videos.isEmpty()) null else position + params.loadSize
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

                    val durationVid = if (durationColumn != null) {
                        it.getLong(durationColumn)
                    } else 0L
                    if (file.isHidden or file.isDirectory or !file.exists() or (durationVid == 0L)) continue

                    val mediaModel = MediaModel(
                        id = id, uri = ContentUris.withAppendedId(
                            AppConfig.VIDEO_MEDIA_URI,
                            id
                        ), title = name, albumId = albumId, albumName = albumName, time = time, file = file, size = imageSize
                    ).apply {
                        duration = durationVid
                    }
                    list.add(mediaModel)
                }
            }
        }
        emit(list)
    }

}