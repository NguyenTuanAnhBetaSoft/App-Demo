package com.betasoft.appdemo.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import com.betasoft.appdemo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

object BitmapUtil {

    @JvmStatic
    fun savePhoto(
        context: Context,
        bitmap: Bitmap,
    ) {
        var folderName = "COMPRESS"
        val dirPath: String =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.path.plus(File.separator)
                .plus(folderName)
        val rootFolder = File(dirPath)
        if (!rootFolder.exists()) {
            rootFolder.mkdirs()
        }

        try {
            val sdf = SimpleDateFormat("ddMMyyyy.hhmmss", Locale.US)
            val fileName = "".plus(sdf.format(Date()))
            val now = Date()
            val quality = 100
            val fos: OutputStream
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues()
                contentValues.put(MediaStore.Images.Media.TITLE, fileName)
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                contentValues.put(
                    MediaStore.Images.Media.DESCRIPTION,
                    context.getString(R.string.app_name)
                )
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                contentValues.put(MediaStore.Images.Media.DATE_ADDED, now.time)
                contentValues.put(MediaStore.Images.Media.DATE_TAKEN, now.time)
                contentValues.put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    (Environment.DIRECTORY_PICTURES).plus(File.separator)
                        .plus(folderName)
                )
                val imageUri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                fos = context.contentResolver.openOutputStream(Objects.requireNonNull(imageUri!!))!!
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, fos)
                fos.close()
                // return getRealPathFromURI(context, imageUri)
            } else {
                val mPath =
                    (rootFolder.absolutePath).plus(File.separator).plus(now.time).plus(".png")
                fos = FileOutputStream(mPath)
                val imageFile = File(mPath)
                if (!imageFile.exists()) {
                    imageFile.createNewFile()
                }
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, fos)
                fos.close()
                val uri = addImageToGallery(context, fileName, imageFile)
                // return getRealPathFromURI(context, uri!!)
            }

        } catch (ex: Exception) {
            return
        }
    }

    private fun addImageToGallery(
        context: Context,
        title: String,
        filepath: File
    ): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
        values.put(MediaStore.Images.Media.DESCRIPTION, context.getString(R.string.app_name))
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.DATA, filepath.toString())
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }




    fun <R> CoroutineScope.executeAsyncTask(
        onPreExecute: () -> Unit,
        doInBackground: () -> R,
        onPostExecute: (R) -> Unit
    ) = launch {
        onPreExecute()
        val result = withContext(Dispatchers.IO) {
            doInBackground()
        }
        onPostExecute(result)
    }
}