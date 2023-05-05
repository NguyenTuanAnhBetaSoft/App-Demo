package com.betasoft.appdemo.utils

import com.betasoft.appdemo.R
import com.betasoft.appdemo.view.base.popup.ActionModel

object Constants {
    const val MY_DIR_ENCRYPT = "/sdcard/.bs/file/Pictures"

    const val APP_NAME = "App Demo"
    const val PROVIDER = ".provider"
    const val SHARE_VIA = "Share Image Via"
    const val INTENT_TYPE_IMAGE = "image/*"
    const val BASE_URL = "https://openart.ai/"
    const val TYPE_JPG = ".jpg"

    //list popup
    val actionMore by lazy {
        val titles = arrayListOf(
            "Share",
            "Delete"
        )
        val icons = arrayListOf(
            R.drawable.ic_share,
            R.drawable.ic_delete
        )
        val actions = mutableListOf<ActionModel>()
        titles.forEachIndexed { index, title ->
            val actionModel = ActionModel(icons[index], title)
            actions.add(actionModel)
        }

        actions
    }
}