package com.betasoft.appdemo.utils.download

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.betasoft.appdemo.utils.Constants
import com.betasoft.appdemo.utils.Utils
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


object ImageUtils {

    fun saveMediaToStorage(bitmap: Bitmap, name: String, context: Context): String {
        // Generating a file name
        var result = ""
        val filename = "${name}${Constants.TYPE_JPG}"
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "$name.jpg"
        )
        // Output stream
        var fos: OutputStream? = null
        // For devices running android >= Q
        if (Utils.isAndroidQ()) {
            // getting the contentResolver
            context.contentResolver?.also { resolver ->
                // Content resolver will process the content values
                val contentValues = ContentValues().apply {
                    // putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                // Inserting the contentValues to
                // contentResolver and getting the Uri
                var check = false
                val list = MediaLoader.loadAllImages(context)
                for (i in 0 until list!!.size) {
                    if (list[i].contains(filename)) {
                        check = true
                    }
                }
                if (check) {
                    result = filePath.toString()
                } else if (!check) {
                    val imageUri: Uri? = resolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
                    )
                    // Opening an output stream with the Uri that we got
                    fos = imageUri?.let { resolver.openOutputStream(it) }
                    result = filePath.toString()
                }
            }
        } else {
            // These for devices running on android < Q
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)

            if (!imagesDir.exists()) {
                imagesDir.mkdirs()
            }

            if (image.exists()) {
                result = filePath.toString()
            } else if (!image.exists()) {
                fos = FileOutputStream(image)
                result = filePath.toString()
            }
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, it)
        }
        return result
    }


}
