package com.betasoft.appdemo.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File


fun Activity.shareMultiple(path: List<String>) {
//    val internalFile = File(path)

    val contentUri = path.flatMap {
        val list = arrayListOf<Uri>()
        list.add(
            FileProvider.getUriForFile(
                this,
                this.packageName + ".provider",
                File(it)
            )
        )
        list
    } as ArrayList<Uri>

    val sharingIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
    sharingIntent.type = "*/*"
    sharingIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
    sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, contentUri)
    launchActivityIntent(Intent.createChooser(sharingIntent, "Share"))
}

fun Activity.launchActivityIntent(intent: Intent) {
    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
    } catch (_: Exception) {

    }
}