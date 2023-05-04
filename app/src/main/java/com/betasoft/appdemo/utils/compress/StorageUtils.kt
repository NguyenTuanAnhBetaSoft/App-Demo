package com.betasoft.appdemo.utils.compress

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.util.Log
import com.betasoft.appdemo.R
import com.betasoft.appdemo.utils.Utils.sdk29andUp
import okio.use
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class StorageUtils {

    /*fun loadImages(context: Context): Flow<List<MediaModel>> {
        return sdk29andUp {
            val images = mutableListOf<MediaModel>()
            try {
                val collection = Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                val projection = arrayOf(
                    Media._ID,
                    Media.DISPLAY_NAME,
                    Media.SIZE,
                    Media.DATE_MODIFIED
                )

                context.contentResolver.query(
                    collection,
                    projection,
                    null, null,
                    null
                )?.use { cursor ->
                    val idColumn = cursor.getColumnIndex(Media._ID)
                    val nameColumn = cursor.getColumnIndex(Media.DISPLAY_NAME)
                    val sizeColumn = cursor.getColumnIndex(Media.SIZE)
                    val dateColumn = cursor.getColumnIndex(Media.DATE_MODIFIED)
                    val dataColumn = cursor.getColumnIndex(Media.DATA)

                    //
                    val albumNameColumn = cursor.getColumnIndex(Media.BUCKET_DISPLAY_NAME)
                    val albumIdColumn = cursor.getColumnIndex(Media.BUCKET_ID)

                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)
                        val displayName = cursor.getString(nameColumn)
                        val modifiedDate = cursor.getLong(dateColumn)
                        val file = File(cursor.getString(dataColumn))
                        val contentUri = ContentUris.withAppendedId(
                            collection, id
                        )
                        val albumId =
                            cursor.getLong(albumIdColumn)
                        val albumName =
                            cursor.getString(albumNameColumn) ?: "Unknown"


                        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                        val imageSize = cursor.getLong(sizeColumn)
                        val day = sdf.format(modifiedDate * 1000)

                        images += MediaModel(
                            id = id, title = displayName,
                            albumId = albumId, albumName = albumName, time = modifiedDate, file = file , uri = contentUri
                        )
                    }

                }

            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            flowOf(images.toList())
        } ?: loadImagesBefore29(context = context)
    }

    private fun loadImagesBefore29(context: Context): Flow<List<MediaModel>> {
        val images = mutableListOf<MediaModel>()
        try {

            val collection = Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                Media._ID,
                Media.DISPLAY_NAME,
                Media.SIZE,
                Media.DATE_MODIFIED,
                Media.DATA
            )
            val selection = "${Media.DATA} like ? "
            val selectionArgs = arrayOf("%${context.getString(R.string.app_name)}%")

            context.contentResolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                null
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndex(Media._ID)
                val nameColumn = cursor.getColumnIndex(Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndex(Media.SIZE)
                val dateColumn = cursor.getColumnIndex(Media.DATE_MODIFIED)
                val dataColumn = cursor.getColumnIndex(Media.DATA)

                //
                val albumNameColumn = cursor.getColumnIndex(Media.BUCKET_DISPLAY_NAME)
                val albumIdColumn = cursor.getColumnIndex(Media.BUCKET_ID)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(nameColumn)
                    val modifiedDate = cursor.getLong(dateColumn)
                    val file = File(cursor.getString(dataColumn))
                    val contentUri = ContentUris.withAppendedId(
                        collection, id
                    )
                    val albumId =
                        cursor.getLong(albumIdColumn)
                    val albumName =
                        cursor.getString(albumNameColumn) ?: "Unknown"


                    val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                    val imageSize = cursor.getLong(sizeColumn)
                    val day = sdf.format(modifiedDate * 1000)

                    images += MediaModel(
                        id = id, title = displayName,
                        albumId = albumId, albumName = albumName, time = modifiedDate, file = file , uri = contentUri
                    )
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return flowOf(images.toList())

    }*/

    fun saveImage(context: Context, image: File, isKeepImage: Boolean) {
        Log.d("4343", "file : ${image.toString()}")
        Log.d("4343fdfdfdfd", "iskeepimage : $isKeepImage")
        sdk29andUp {
            try {
                val collection = Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                var path = Environment.DIRECTORY_PICTURES + "/" + context.getString(R.string.app_name)

                if (isKeepImage) {
                }

                val values = ContentValues().apply {
                    put(Media.DISPLAY_NAME, image.name)
                    put(Media.SIZE, image.length())
                    put(Media.MIME_TYPE, "image/jpg")
                    put(Media.RELATIVE_PATH, path)
                }

                context.contentResolver.insert(
                    collection, values
                )?.also { uri ->
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        val fis = FileInputStream(image)
                        var length: Int
                        val buffer = ByteArray(8192)
                        while (fis.read(buffer).also { length = it } > 0)
                            outputStream.write(buffer, 0, length)
                    }
                    println(uri)
                } ?: throw IOException("Error creating entry in MediaStore")
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } ?: saveImageBefore29(context, image, isKeepImage)
    }

    private fun saveImageBefore29(context: Context, image: File, isKeepImage: Boolean) {
        try {
            val resolver = context.contentResolver
            val collection = Media.EXTERNAL_CONTENT_URI

            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .absolutePath + "/${context.getString(R.string.app_name)}"

            val directory = File(dir)
            if (!directory.exists())
                directory.mkdirs()

            val savedImage = File(dir, image.name)
            val values = ContentValues().apply {
                put(Media.DISPLAY_NAME, image.name)
                put(Media.SIZE, image.length())
//                put(Media.DATE_MODIFIED, image.lastModified())
                put(Media.MIME_TYPE, "image/jpg")
                put(Media.DATA, savedImage.path)
            }

            resolver.insert(collection, values)?.also { uri ->
                resolver.openOutputStream(uri)?.use { outputStream ->
                    val fis = FileInputStream(image)
                    var length: Int
                    val buffer = ByteArray(8192)
                    while (fis.read(buffer).also { length = it } > 0)
                        outputStream.write(buffer, 0, length)
                }
            } ?: throw IOException("Error Writing to MediaStore")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}