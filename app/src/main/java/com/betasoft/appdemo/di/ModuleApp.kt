package com.betasoft.appdemo.di

import android.content.Context
import androidx.room.Room
import com.betasoft.appdemo.data.local.roomDb.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleApp {
    private const val database = "App Demo"

    @Singleton
    @Provides
    fun providerDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(
                context,
                AppDatabase::class.java, database
            )
            .fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideImageLocalDao(db: AppDatabase) = db.ImageLocalDao()

}