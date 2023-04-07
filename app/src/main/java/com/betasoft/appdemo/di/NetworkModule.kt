package com.betasoft.appdemo.di

import com.betasoft.appdemo.data.api.RemoteServices
import com.betasoft.appdemo.utils.Constants.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesOkhttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient
            .Builder().writeTimeout(10 * 1000.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(6 * 1000.toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(6 * 1000.toLong(), TimeUnit.MILLISECONDS).addInterceptor { chain ->
                val request =
                    chain.request()
                        .newBuilder()
                        .build()
                chain.proceed(request)
            }
        return builder.build()
    }


    @Provides
    @Singleton
    fun providesHttpLoginInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun providesMoshiConverterFactory(): MoshiConverterFactory {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    @Named("RemoteApi")
    fun providesRemote(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory,
    ): Retrofit {
        return Retrofit.Builder().addConverterFactory(moshiConverterFactory)
            .baseUrl(BASE_URL)
            .client(okHttpClient).build()
    }

    @Singleton
    @Provides
    fun provideRemoteAPI(@Named("RemoteApi") retrofit: Retrofit): RemoteServices {
        return retrofit.create(RemoteServices::class.java)
    }

}
