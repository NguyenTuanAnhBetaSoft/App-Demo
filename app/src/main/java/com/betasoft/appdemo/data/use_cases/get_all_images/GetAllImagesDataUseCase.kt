package com.betasoft.appdemo.data.use_cases.get_all_images

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.data.repository.ImagesPagingSource

class GetAllImagesDataUseCase(
    private val imagesPagingSource: ImagesPagingSource
) {
    suspend operator fun invoke(
        params: LoadParams<Int>
    ): LoadResult<Int, MediaModel> {
        return imagesPagingSource.load(params = params)
    }
}