package com.betasoft.appdemo.data.model

import com.betasoft.appdemo.data.api.responseremote.ItemsItem

data class ImageResult(val curPage: Int, val items: List<ItemsItem>?, val cursor: String?)
