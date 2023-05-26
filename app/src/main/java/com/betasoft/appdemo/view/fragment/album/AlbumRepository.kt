package com.betasoft.appdemo.view.fragment.album

import android.content.ContentUris
import android.provider.MediaStore
import com.betasoft.appdemo.App
import com.betasoft.appdemo.di.AppContext
import com.betasoft.appdemo.utils.AppConfig.IMAGES_MEDIA_URI
import com.betasoft.appdemo.view.fragment.searchphoto.Album
import com.betasoft.appdemo.view.fragment.searchphoto.PhotoCompression
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AlbumRepository @Inject constructor(
    @AppContext private val context: App,
) {

    suspend fun getAlbums(): List<Album> {
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        val sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"

        val albums = mutableListOf<Album>()

        withContext(Dispatchers.IO) {
            val cursor = context.contentResolver.query(
                IMAGES_MEDIA_URI,
                projection,
                null,
                null,
                sortOrder
            )

            cursor?.let {
                val bucketIdColumn = cursor.getColumnIndexOrThrow(projection[0])
                val bucketNameColumn = cursor.getColumnIndexOrThrow(projection[1])

                val albumMap = mutableMapOf<Long, Album>()

                while (cursor.moveToNext()) {
                    val bucketId = cursor.getLong(bucketIdColumn)
                    val bucketName = cursor.getString(bucketNameColumn)

                    if (!albumMap.containsKey(bucketId)) {
                        albumMap[bucketId] = Album(bucketId, bucketName, null)
                    }
                }

                // Get the first photo of each album
                albumMap.forEach { (bucketId, album) ->
                    album.totalImages = getImageCountForAlbum(bucketId)
                    val firstPhoto = getFirstPhotoFromAlbum(bucketId)
                    album.firstPhoto = firstPhoto
                    albums.add(album)
                }

                cursor.close()
            }
        }

        return albums
    }

    private fun getFirstPhotoFromAlbum(albumId: Long): PhotoCompression? {
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE
        )
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(albumId.toString())
        val sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"

        context.contentResolver.query(
            IMAGES_MEDIA_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            if (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(projection[0]))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]))
                val size = cursor.getLong(cursor.getColumnIndexOrThrow(projection[2]))

                val file = File(path)
                if (file.exists()) {
                    return PhotoCompression(
                        id = id,
                        uri = ContentUris.withAppendedId(IMAGES_MEDIA_URI, id),
                        file = file,
                        size = size
                    )
                }
            }
        }

        return null
    }

    private fun getImageCountForAlbum(albumId: Long): Int {
        val projection = arrayOf("COUNT(_id)")
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(albumId.toString())

        context.contentResolver.query(
            IMAGES_MEDIA_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val countIndex = cursor.getColumnIndexOrThrow("COUNT(_id)")
                return cursor.getInt(countIndex)
            }
        }

        return 0
    }






}


