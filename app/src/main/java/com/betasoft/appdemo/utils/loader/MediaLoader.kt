package com.betasoft.appdemo.utils.loader

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.betasoft.appdemo.App
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.di.AppContext
import com.betasoft.appdemo.utils.AppConfig.IMAGES_MEDIA_URI
import com.betasoft.appdemo.utils.AppConfig.VIDEO_MEDIA_URI
import com.betasoft.appdemo.utils.Utils.isAndroidQ
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MediaLoader @Inject constructor(
    @AppContext private val context: App,
) {
    companion object {
        const val typeVideo = -1
        const val typeImage = -2
    }

    suspend fun loadAllVideos(
        imagesOffset: Int,
        imagesLimit: Int,
    ): Flow<List<MediaModel>> {
        return getListFromUri(
            imagesOffset,
            imagesLimit,
            VIDEO_MEDIA_URI, typeVideo
        )
    }


     suspend fun loadAllImages(
        imagesOffset: Int,
        imagesLimit: Int,
    ): Flow<List<MediaModel>> {
        return getListFromUri(
            imagesOffset,
            imagesLimit, IMAGES_MEDIA_URI, typeImage
        )
    }


    suspend fun getListFromUri(
        imagesOffset: Int,
        imagesLimit: Int,
        uri: Uri, type: Int,
        selection: String? = null,
        selectionArgs: Array<String>? = null
    ): Flow<List<MediaModel>> = flow {
        val list = mutableListOf<MediaModel>()
        withContext(Dispatchers.IO) {
            val projection =
                if (type == typeImage) {
                    arrayOf(
                        MediaStore.MediaColumns._ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.DATE_MODIFIED,
                        MediaStore.Images.Media.DATA
                    )
                } else {
                    arrayOf(
                        MediaStore.MediaColumns._ID,

                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Video.Media.BUCKET_ID,
                        MediaStore.Video.Media.DATE_MODIFIED,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.VideoColumns.DURATION
                    )
                }


            createCursor(
                imagesOffset,
                imagesLimit,
                context.contentResolver,
                uri,
                projection,
                selection,
                selectionArgs
            ).use { cursor ->
                cursor?.let {
                    val idColumn = it.getColumnIndexOrThrow(projection[0])
                    val albumIdColumn = it.getColumnIndexOrThrow(projection[2])
                    val albumNameColumn =
                        it.getColumnIndexOrThrow(projection[1])
                    val pathColumn =
                        it.getColumnIndexOrThrow(projection[4])
                    val timeColumn = it.getColumnIndexOrThrow(projection[3])
                    val durationColumn =
                        if (type == typeVideo) it.getColumnIndexOrThrow(projection[5])
                        else null

                    it.moveToPosition(imagesOffset)

                    while (it.moveToNext() && it.position <= imagesOffset + imagesLimit) {
                        // Get values of columns for a given.
                        Log.d("6565656", "imageoffset = $imagesOffset")
                        Log.d("6565656", "imagesLimit = $imagesLimit")
                        Log.d("6565656", "itMoveTonext = ${it.moveToNext()}")
                        Log.d("6565656", "it.Position = ${it.position}")

                        val path = it.getString(pathColumn) ?: "Unknown"
                        val file = File(path)
                        val id = it.getLong(idColumn)
                        val name = file.name
                        val albumId =
                            it.getLong(albumIdColumn)
                        val albumName =
                            it.getString(albumNameColumn) ?: "Unknown"
                        val time = it.getLong(timeColumn)
                        val durationVid = if (durationColumn != null) {
                            it.getLong(durationColumn)
                        } else 0L
                        if (file.isHidden or file.isDirectory or !file.exists() or (type == typeVideo && durationVid == 0L)) continue



                        val mediaModel = MediaModel(
                            id, ContentUris.withAppendedId(
                                uri,
                                id
                            ), name, albumId, albumName, time, file
                        ).apply {
                            duration = durationVid
                        }
                        list.add(mediaModel)
                    }
                    it.close()
                    Log.d("6565656", "close")
                }

            }
        }
        emit(list)
    }


    fun createCursor(
        imagesOffset: Int,
        imagesLimit: Int,
        contentResolver: ContentResolver,
        collection: Uri,
        projection: Array<String>,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
    ): Cursor? = when {
        isAndroidQ() -> {
            contentResolver.query(collection, null, selection, selectionArgs, "date_added DESC")
        }
        else -> {
            contentResolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                "date_added DESC"
            )
        }
    }


}


