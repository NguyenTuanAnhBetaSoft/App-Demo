package com.betasoft.appdemo.utils.download

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.betasoft.appdemo.utils.Utils
import java.io.File

object MediaLoader {
    fun loadAllImages(
        context: Context,
    ): List<String>? {
        val uri = getUri()
        val selectionArgs =
            arrayOf("image/png", "image/jpg", "image/jpeg", "image/bmp", "image/gif")
        val selection = getWhereClause(selectionArgs)
        return getListFromUri(context, uri, selection, selectionArgs, false)
    }

    private fun getWhereClause(clause: Array<String>): String {
        val where = StringBuilder("(")
        for (i in clause.indices) {
            if (i < clause.size - 1) {
                where.append(MediaStore.Files.FileColumns.MIME_TYPE + " =? OR ")
            } else {
                where.append(MediaStore.Files.FileColumns.MIME_TYPE + " =? )")
            }
        }
        return where.toString()
    }

    private fun getUri(): Uri {
        return if (Utils.isAndroidQ()) {
            MediaStore.Files.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Files.getContentUri("external")
        }
    }


    private fun getListFromUri(
        context: Context,
        uri: Uri,
        selection: String,
        selectionArg: Array<String>, orderAscending: Boolean,
    ): List<String>? {
        val list: MutableList<String> = ArrayList<String>()
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
        )
        val cursor = createCursor(
            context.contentResolver,
            uri,
            projection,
            selection,
            selectionArg,
            orderAscending
        )
        if (cursor != null) {
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

            while (cursor.moveToNext()) {
                val path = cursor.getString(pathColumn)
                val file = File(path)
                var albumName = cursor.getString(nameColumn)
                if (!file.exists() || file.isHidden) continue
                if (albumName == null) {
                    albumName = "_"
                }
                val photoName = albumName

                list.add(photoName)
            }
            cursor.close()
        }
        return list
    }

    private fun createCursor(
        contentResolver: ContentResolver,
        collection: Uri,
        projection: Array<String>,
        whereCondition: String,
        selectionArgs: Array<String>, orderAscending: Boolean,
    ): Cursor? {
        return if (Utils.isAndroidQ()) {
            val selection = createSelectionBundle(whereCondition, selectionArgs, orderAscending)
            contentResolver.query(collection, projection, selection, null)
        } else {
            contentResolver.query(collection, projection, whereCondition, selectionArgs, null)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createSelectionBundle(
        whereCondition: String,
        selectionArgs: Array<String>,
        orderAscending: Boolean,
    ): Bundle {
        val bundle = Bundle()
        val values = arrayOf(MediaStore.Files.FileColumns.DATE_ADDED)
        bundle.putStringArray(
            ContentResolver.QUERY_ARG_SORT_COLUMNS,
            values
        )
        val orderDirection: Int
        orderDirection = if (orderAscending) {
            ContentResolver.QUERY_SORT_DIRECTION_ASCENDING
        } else {
            ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
        }
        bundle.putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, orderDirection)
        bundle.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, whereCondition)
        bundle.putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs)
        return bundle
    }


}