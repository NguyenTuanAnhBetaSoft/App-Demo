package com.betasoft.appdemo.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.utils.AppConfig
import com.betasoft.appdemo.utils.loader.MediaLoader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideosPagingSource @Inject constructor(
    private val localStorage: MediaLoader
) : PagingSource<Int, MediaModel>() {
    override fun getRefreshKey(state: PagingState<Int, MediaModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaModel> {
        val position = params.key ?: -1
        return try {
            val videos: MutableList<MediaModel> = mutableListOf()
            localStorage.getListFromUri(
                imagesOffset = position,
                imagesLimit = params.loadSize,
                AppConfig.VIDEO_MEDIA_URI, MediaLoader.typeVideo
            ).collect { imageDataList ->
                videos.addAll(imageDataList)
            }
            LoadResult.Page(
                data = videos,
                prevKey = if (position == -1) null else position,
                nextKey = if (videos.isEmpty()) null else position + params.loadSize
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}