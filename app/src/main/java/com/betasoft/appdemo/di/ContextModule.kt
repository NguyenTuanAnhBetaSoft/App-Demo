package com.betasoft.appdemo.di

import android.content.Context
import com.betasoft.appdemo.App
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContextModule {
    @AppContext
    @Provides
    @Singleton
    fun provideAppLocation(@ApplicationContext context: Context): App = context as App


}