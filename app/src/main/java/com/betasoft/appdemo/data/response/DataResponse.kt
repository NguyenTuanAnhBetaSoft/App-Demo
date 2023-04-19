package com.betasoft.appdemo.data.response

import okhttp3.ResponseBody

sealed class DataResponse<T> constructor(val loadingStatus: LoadingStatus) {
    class DataLoading<T>(loadingType: LoadingStatus) : DataResponse<T>(loadingType)
    class DataIdle<T> : DataResponse<T>(LoadingStatus.Idle)
    data class DataError<T, V>(val errorData: V?=null) : DataResponse<T>(LoadingStatus.Error)
    data class DataSuccess<T>(val body: T) : DataResponse<T>(LoadingStatus.Success)
}
