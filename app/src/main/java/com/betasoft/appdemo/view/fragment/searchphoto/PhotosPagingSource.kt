package com.betasoft.appdemo.view.fragment.searchphoto

import android.content.ContentUris
import android.database.Cursor
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.betasoft.appdemo.App
import com.betasoft.appdemo.di.AppContext
import com.betasoft.appdemo.utils.AppConfig.IMAGES_MEDIA_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

// ...

class PhotosPagingSource @Inject constructor(
    @AppContext private val context: App
) : PagingSource<Int, PhotoCompression>() {
    private val projection =
        arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID
        )

    private var searchQuery: String = ""
    private var bucketIdQuery: String = ""

    // ...

    override fun getRefreshKey(state: PagingState<Int, PhotoCompression>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoCompression> {
        val position = params.key ?: -1
        val query = if (searchQuery.isNotEmpty()) searchQuery else null
        val bucketId = if (bucketIdQuery.isNotEmpty())  bucketIdQuery else null

        return try {
            val images: MutableList<PhotoCompression> = mutableListOf()
            getListFromUri(
                imagesOffset = position,
                imagesLimit = params.loadSize,
                query = query,
                bucketId = bucketId
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
        query: String?,
        bucketId: String?
    ): Flow<List<PhotoCompression>> = flow {
        val list = mutableListOf<PhotoCompression>()
        try {
            withContext(Dispatchers.IO) {
                val cursor = if (query !=null) {
                    searchImages(query = query)
                } else if (bucketId != null) {
                    getImagesByBucketId(bucketId = bucketId)
                } else {
                    getDefaultImagesCursor()
                }

                /////////////////////////////////////////////

                cursor?.let {
                    val idColumn = cursor.getColumnIndexOrThrow(projection[0])
                    val pathColumn =
                        cursor.getColumnIndexOrThrow(projection[1])
                    val sizeColumn = cursor.getColumnIndexOrThrow(projection[2])

                    val albumNameColumn =
                        cursor.getColumnIndexOrThrow(projection[3])
                    val albumIdColumn = cursor.getColumnIndexOrThrow(projection[4])


                    it.moveToPosition(imagesOffset)
                    while (it.moveToNext() && it.position <= imagesOffset + imagesLimit) {
                        // Get values of columns for a given image.
                        val path = it.getString(pathColumn) ?: "Unknown"
                        val file = File(path)
                        val id = it.getLong(idColumn)
                        val name = file.name
                        val imageSize = cursor.getLong(sizeColumn)

                        val albumId =
                            it.getLong(albumIdColumn)
                        val albumName =
                            it.getString(albumNameColumn) ?: "Unknown"

                        if (file.isHidden || file.isDirectory || !file.exists()) continue

                        val mediaModel = PhotoCompression(
                            id = id, uri = ContentUris.withAppendedId(
                                IMAGES_MEDIA_URI,
                                id
                            ), title = name, albumId = albumId, albumName = albumName, file = file, size = imageSize
                        )
                        list.add(mediaModel)
                    }
                }
            }
            emit(list)
        } catch (_: Exception) {
            // Handle exceptions
        }
    }

    private fun getDefaultImagesCursor(): Cursor? {
        return context.contentResolver.query(
            IMAGES_MEDIA_URI,
            projection,
            null,
            null,
            "date_added DESC"
        )
    }

    private fun searchImages(query: String): Cursor? {
        val selection = "${MediaStore.Images.Media.DISPLAY_NAME} LIKE ?"
        val selectionArgs = arrayOf("%$query%")

        return context.contentResolver.query(
            IMAGES_MEDIA_URI,
            projection,
            selection,
            selectionArgs,
            "date_added DESC"
        )
    }

    private fun getImagesByBucketId(bucketId: String): Cursor? {
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(bucketId)

        return context.contentResolver.query(
            IMAGES_MEDIA_URI,
            projection,
            selection,
            selectionArgs,
            "date_added DESC"
        )
    }

    fun setSearchQuery(query: String) {
        searchQuery = query
    }
    fun setBucketIdQuery(bucketId: String) {
        bucketIdQuery = bucketId
    }

}


//class PhotosPagingSource @Inject constructor(
//    @AppContext private val context: App
//) : PagingSource<Int, PhotoCompression>() {
//    private val projection =
//        arrayOf(
//            MediaStore.MediaColumns._ID,
//            MediaStore.Images.Media.DATA,
//            MediaStore.Images.Media.SIZE
////            MediaStore.Images.Media.DATE_MODIFIED
////            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
////            MediaStore.Images.Media.BUCKET_ID,
//
//        )
//    private val cursor = when {
//        isAndroidQ() -> {
//            context.contentResolver.query(
//                IMAGES_MEDIA_URI,
//                null,
//                null,
//                null,
//                "_size DESC"
//            )
//        }
//
//        else -> {
//            context.contentResolver.query(
//                IMAGES_MEDIA_URI,
//                projection,
//                null,
//                null,
//                "_size DESC"
//            )
//        }
//    }
//
//    private val idColumn = cursor?.getColumnIndexOrThrow(projection[0])
//    private val pathColumn =
//        cursor?.getColumnIndexOrThrow(projection[1])
//    private val sizeColumn = cursor?.getColumnIndexOrThrow(projection[2])
////    private val albumIdColumn = cursor?.getColumnIndexOrThrow(projection[2])
////    private val albumNameColumn =
////        cursor?.getColumnIndexOrThrow(projection[1])
////    private val timeColumn = cursor?.getColumnIndexOrThrow(projection[3])
////    private val dateColumn = cursor?.getColumnIndexOrThrow(projection[6])
//
//
//    override fun getRefreshKey(state: PagingState<Int, PhotoCompression>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoCompression> {
//        val position = params.key ?: -1
//        return try {
//            val images: MutableList<PhotoCompression> = mutableListOf()
//            getListFromUri(
//                imagesOffset = position,
//                imagesLimit = params.loadSize
//            ).collect { imageDataList ->
//                images.addAll(imageDataList)
//            }
//
//            LoadResult.Page(
//                data = images,
//                prevKey = if (position == -1) null else position,
//                nextKey = if (images.isEmpty()) null else position + params.loadSize
//            )
//        } catch (exception: Exception) {
//            LoadResult.Error(exception)
//        }
//    }
//
//    private suspend fun getListFromUri(
//        imagesOffset: Int,
//        imagesLimit: Int,
//    ): Flow<List<PhotoCompression>> = flow {
//        val list = mutableListOf<PhotoCompression>()
//        try {
//            withContext(Dispatchers.IO) {
//                cursor?.let {
//
//                    it.moveToPosition(imagesOffset)
//                    while (it.moveToNext() && it.position <= imagesOffset + imagesLimit) {
//                        // Get values of columns for a given.
//                        val path = it.getString(pathColumn!!) ?: "Unknown"
//                        val file = File(path)
//                        val id = it.getLong(idColumn!!)
//                        val name = file.name
//                        val imageSize = cursor.getLong(sizeColumn!!)
//
////                        val albumId =
////                            it.getLong(albumIdColumn!!)
////                        val albumName =
////                            it.getString(albumNameColumn!!) ?: "Unknown"
////                        val time = it.getLong(timeColumn!!)
////                        val modifiedDate = cursor.getLong(dateColumn!!)
////                        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
////                        val day = sdf.format(modifiedDate * 1000)
//
//                        if (file.isHidden or file.isDirectory or !file.exists()) continue
//
//                        val mediaModel = PhotoCompression(
//                            id = id,
//                            uri = ContentUris.withAppendedId(
//                                IMAGES_MEDIA_URI,
//                                id
//                            ),
//                            title = name,
////                            albumId = albumId,
////                            albumName = albumName,
////                            time = time,
//                            file = file,
//                            size = imageSize
//                        )
//                        list.add(mediaModel)
//                    }
//                }
//            }
//            emit(list)
//        } catch (_: Exception) {
//
//        }
//
//
//    }
//
//
//}

