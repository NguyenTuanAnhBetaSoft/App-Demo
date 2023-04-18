package com.betasoft.appdemo.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.util.*

object Utils {
    fun isAndroidQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun isTIRAMISU(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun openShareWindow(context: Context, fileName: String?) {
        val fileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(fileDir, "$fileName${Constants.TYPE_JPG}")

        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + Constants.PROVIDER,
            Objects.requireNonNull(file)
        )
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, fileName)
        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, Constants.APP_NAME)
        // setting type to image
        intent.type = Constants.INTENT_TYPE_IMAGE
        // calling startActivity() to share
        context.startActivity(Intent.createChooser(intent, Constants.SHARE_VIA))

    }
}