package com.betasoft.appdemo.data.api

import com.betasoft.appdemo.data.api.responseremote.DataResponseRemote
import retrofit2.http.*


interface RemoteServices {


    @GET("api/feed/community")
    suspend fun fetchImagePagingList(
        @Query("cursor") cursor: String,
    ): DataResponseRemote?

    @GET("api/feed/community")
    suspend fun fetchImageList(
    ): DataResponseRemote?


}
