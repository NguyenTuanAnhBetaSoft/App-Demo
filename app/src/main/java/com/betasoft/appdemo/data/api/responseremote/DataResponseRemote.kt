package com.betasoft.appdemo.data.api.responseremote

data class DataResponseRemote(
    val items: List<Item>,
    val nextCursor: String
)