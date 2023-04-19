package com.betasoft.appdemo.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.betasoft.appdemo.R
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.util.*

object Utils {
    fun isAndroidQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun isTIRAMISU(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun isAndroidR(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
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

    //
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val STORAGE_PERMISSION_STORAGE_SCOPE_HIGH_API = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,

        )

    private val STORAGE_PERMISSION_STORAGE_SCOPE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val STORAGE_PERMISSION_UNDER_STORAGE_SCOPE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,

        )

    private fun allPermissionGrant(context: Context, intArray: Array<String>): Boolean {
        var isGranted = true
        for (permission in intArray) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                isGranted = false
                break
            }
        }
        return isGranted
    }

    fun storagePermissionGrant(context: Context): Boolean {
        return allPermissionGrant(context, getStoragePermissions())
    }

    fun getStoragePermissions(): Array<String> {

        return if (isTIRAMISU()) {
            STORAGE_PERMISSION_STORAGE_SCOPE_HIGH_API
        } else {
            if (isAndroidR()) {
                STORAGE_PERMISSION_STORAGE_SCOPE
            } else {
                STORAGE_PERMISSION_UNDER_STORAGE_SCOPE
            }
        }
    }

    private fun hasShowRequestPermissionRationale(
        activity: Activity,
        vararg permissions: String?,
    ): Boolean {

        for (permission in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    permission!!
                )
            ) {
                return true
            }
        }

        return false
    }

    fun showAlertPermissionNotGrant(
        view: View,
        activity: Activity,
        permissions: Array<out String?>
    ) {
        if (!hasShowRequestPermissionRationale(activity, *permissions)) {
            val snackBar = Snackbar.make(
                view,
                activity.getString(R.string.goto_settings),
                Snackbar.LENGTH_LONG
            )
            snackBar.setAction(
                activity.getString(R.string.settings)
            ) {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }
            snackBar.show()
        } else {
            Toast.makeText(
                activity,
                activity.getString(R.string.grant_permission),
                Toast.LENGTH_SHORT
            ).show()
            //activity.finish()
        }
    }


}