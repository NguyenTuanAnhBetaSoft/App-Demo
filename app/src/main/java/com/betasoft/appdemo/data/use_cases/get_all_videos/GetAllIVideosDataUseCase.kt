package com.betasoft.appdemo.data.use_cases.get_all_videos

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.data.repository.VideosPagingSource

class GetAllIVideosDataUseCase(
    private val videosPagingSource: VideosPagingSource
) {
    suspend operator fun invoke(
        params: LoadParams<Int>
    ): LoadResult<Int, MediaModel> {
        return videosPagingSource.load(params = params)
    }
}