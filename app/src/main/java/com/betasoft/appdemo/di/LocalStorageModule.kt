package com.betasoft.appdemo.di

import com.betasoft.appdemo.data.repository.ImagesPagingSource
import com.betasoft.appdemo.data.use_cases.UseCases
import com.betasoft.appdemo.data.use_cases.get_all_images.GetAllImagesDataUseCase
import com.betasoft.appdemo.utils.download.MediaLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    @Provides
    @Singleton
    fun provideUseCases(
        localStorage: MediaLoader,
        imagesPagingSource: ImagesPagingSource
    ): UseCases {
        return UseCases(
            getAllImagesDataUseCase = GetAllImagesDataUseCase(
                imagesPagingSource = imagesPagingSource
            )
        )
    }
}