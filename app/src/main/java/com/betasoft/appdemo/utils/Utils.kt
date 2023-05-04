package com.betasoft.appdemo.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.betasoft.appdemo.R
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

object Utils {
    fun isAndroidQ(): Boolean {
        return SDK_INT >= Build.VERSION_CODES.Q
    }

    fun isTIRAMISU(): Boolean {
        return SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun isAndroidR(): Boolean {
        return SDK_INT >= Build.VERSION_CODES.R
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

    inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? =
        when {
            SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
            else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
        }

    inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? =
        when {
            SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
            else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
        }

    @Suppress("DEPRECATION")
     fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

     fun getReadableFileSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1000.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1000.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }


     fun Uri.getImageDimensions(context: Context): String {
        val inputStream = context.contentResolver.openInputStream(this)!!
        val exif = ExifInterface(inputStream)

        val width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 150)
        val height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 150)
        return "$width, $height"
    }

    inline fun <T> sdk29andUp(onSdk29 : () -> T) : T? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) onSdk29()
        else null



}