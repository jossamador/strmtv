package com.example.strmtv.di

import com.example.strmtv.data.model.remote.LocalApiService
import com.example.strmtv.data.model.remote.TMDBApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTMDBApiService(): TMDBApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDBApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLocalApiService(): LocalApiService {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/") // tu API local
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocalApiService::class.java)
    }
}