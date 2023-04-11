package com.betasoft.appdemo.utils.download

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object DownloadUrl {
    private var client = OkHttpClient()

    fun download(
        url: String,
        nameFile: String?,
        haveSave: Boolean,
        context: Context
    ): String? {
        var result: String? = null
        val builder = Request.Builder()
        builder.url(url)
        val request: Request = builder.build()
        return try {
            val response = client.newCall(request).execute()
            val image = response.body!!.bytes()
            var bitmap: Bitmap? = null
            if (image.isNotEmpty()) {
                bitmap = BitmapFactory.decodeByteArray(
                    image, 0,
                    image.size
                )
            }
            if (haveSave) {
                val filePath = ImageUtils.saveMediaToStorage(bitmap!!, nameFile!!, context)
                result = filePath
            }
            return result
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}